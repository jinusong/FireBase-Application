package com.jinwoo.dsmjumpup

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jinwoo.dsmjumpup.model.ChatMessageModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {

    private val MESSAGE_CHILD = "message"

    private lateinit var mGoogleApiClient: GoogleSignInClient

    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference

    private lateinit var mFirebaseUser: FirebaseUser
    private lateinit var mFirebaseAuth: FirebaseAuth

    private lateinit var mUserName: String
    private lateinit var mPhotoUrl: String

    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<ChatMessageModel, MessageViewHolder>

    private lateinit var mCurrentUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase.getReference(MESSAGE_CHILD)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleSignIn.getClient(this@MainActivity, gso)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth.currentUser!!

        mCurrentUid = mFirebaseUser.uid

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase.getReference()

        mUserName = mFirebaseUser.displayName!!
        if (mFirebaseUser.photoUrl != null) {
            mPhotoUrl = mFirebaseUser.photoUrl.toString()
        }

        activity_button_send.setOnClickListener {
            var chatMessage = ChatMessageModel(mUserName, activity_edittext_message.text.toString(), mPhotoUrl, mCurrentUid)
            mDatabaseReference.child(MESSAGE_CHILD).push().setValue(chatMessage)
            activity_edittext_message.setText("")
        }

        var query = mDatabaseReference.child(MESSAGE_CHILD)

        var options = FirebaseRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query, ChatMessageModel::class.java)
                .setLifecycleOwner(this)
                .build()

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<ChatMessageModel, MessageViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {

                var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
                return MessageViewHolder(view)
            }

            override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: ChatMessageModel) {
                holder.messageTextView.setText(model.message)
                holder.nameTextView.setText(model.name)

                if(model.uid != null && model.uid.equals(mCurrentUid)) {
                    holder.profileImageView.visibility = View.GONE
                    holder.nameTextView.visibility = View.GONE

                    holder.messageTextView.setBackgroundResource(R.drawable.rightbubble)

                    holder.linearlayout.gravity = Gravity.RIGHT
                } else {
                    holder.profileImageView.visibility = View.VISIBLE

                    if (model.photoUrl == null) {
                        holder.profileImageView.setImageDrawable(ContextCompat.getDrawable(this@MainActivity,
                                R.drawable.ic_account_circle_black_24dp))
                    } else {
                        Glide.with(this@MainActivity).load(model.photoUrl).into(holder.profileImageView)
                    }

                    holder.messageTextView.setBackgroundResource(R.drawable.leftbubble)

                    holder.nameTextView.visibility = View.VISIBLE
                    holder.nameTextView.setText(model.name)

                    holder.linearlayout.gravity = Gravity.LEFT
                }
            }
        }
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.setAdapter(mFirebaseAdapter)

        mFirebaseAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)

                var messageCount = mFirebaseAdapter.itemCount
                var layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                var lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (messageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart)
                }
            }
        })

        recyclerView.addOnLayoutChangeListener{
            v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                v.postDelayed({
                    kotlin.run { recyclerView.smoothScrollToPosition(mFirebaseAdapter.itemCount) }
                },100)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter.stopListening()
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linearlayout: LinearLayout
        var profileImageView: CircleImageView
        var messageTextView: TextView
        var nameTextView: TextView

        init {
            linearlayout =  itemView.findViewById(R.id.linearlayout)
            profileImageView = itemView.findViewById(R.id.itemmessage_imageview_profile)
            messageTextView = itemView.findViewById(R.id.itemmessage_textview_message)
            nameTextView = itemView.findViewById(R.id.itemmessage_textview_name)
        }
    }
}