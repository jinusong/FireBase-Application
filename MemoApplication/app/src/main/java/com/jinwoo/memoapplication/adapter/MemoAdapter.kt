package com.jinwoo.memoapplication.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.jinwoo.memoapplication.R
import com.jinwoo.memoapplication.RecyclerViewClickListener
import com.jinwoo.memoapplication.model.MemoModel
import kotlinx.android.synthetic.main.item_layout.view.*

class MemoAdapter(ref: DatabaseReference): RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {

    var memoList = ArrayList<MemoModel>()
    var mMemoIds = ArrayList<String>()
    var mDatabaseReference: DatabaseReference
    lateinit var mListener: RecyclerViewClickListener

    fun setOnClickListener(mListener: RecyclerViewClickListener){
        this.mListener = mListener
    }

    init{
        mDatabaseReference = ref
        mDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mMemoIds.clear()
                memoList.clear()

                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    mMemoIds.add(snapshot.key!!)
                    memoList.add(snapshot.getValue(MemoModel::class.java)!!)
                }
                notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): MemoViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MemoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return memoList.size
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        var memo: MemoModel = memoList.get(position)
        holder.titleTextView.setText(memo.title)
        holder.dateTextView.setText(memo.date)
        var key: String = mMemoIds.get(position)
        if(mListener != null) {
            val pos = position
            val memoModel: MemoModel = memo
            val memoKey: String = key

            holder.itemView.setOnClickListener{
                mListener.onItemClicked(pos, memoModel, memoKey)
            }

            holder.itemView.setOnLongClickListener {
                mListener.onItemLongClicked(pos, memoModel, memoKey)
                return@setOnLongClickListener true
            }
        }
    }

    class MemoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView
        var dateTextView: TextView
        init {
            titleTextView = itemView.findViewById(R.id.itemmeomo_textview_title)
            dateTextView = itemView.findViewById(R.id.itemmeomo_textview_date)
        }
    }
}