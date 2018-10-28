package com.jinwoo.memoapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import com.jinwoo.memoapplication.model.MemoModel
import kotlinx.android.synthetic.main.activity_memo.*
import java.text.SimpleDateFormat
import java.util.*

class MemoActivity:AppCompatActivity() {

    lateinit var mDatabaseReference: DatabaseReference

    val REQUIRED_TITLE: String = "제목을 입력하세요."
    val REQUIRED_CONTENTS: String = "내용을 입력하세요."
    var mMemoKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        var intent = intent
        if (intent != null){
            mMemoKey =  intent.getStringExtra("memokey")
            var title: String? = intent.getStringExtra("title")
            var contents: String? = intent.getStringExtra("contents")

            memoactivity_edittext_title.setText(title)
            memoactivity_edittext_content.setText(contents)
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference()

        memoactivity_button_save.setOnClickListener {

            var title: String = memoactivity_edittext_title.text.toString()
            var content: String = memoactivity_edittext_content.text.toString()
            var dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm")
            var model= MemoModel(title, content, dateFormat.format(Date()))

            if(mMemoKey == null){

                if (TextUtils.isEmpty(title)) {
                    memoactivity_edittext_title.setError(REQUIRED_TITLE)
                    return@setOnClickListener
                }

                if (TextUtils.isEmpty(content)) {
                    memoactivity_edittext_content.setError(REQUIRED_CONTENTS)
                    return@setOnClickListener
                }

                mDatabaseReference.child(resources.getString(R.string.notechild))
                        .push().setValue(model).addOnSuccessListener {
                            Toast.makeText(this, "저장완료", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "저장실패", Toast.LENGTH_SHORT).show()
                        }
                finish()
            }  else {
                Log.d("MemoActivity", "update item: " + mMemoKey)
                var path: String = resources.getString(R.string.notechild) + "/" + mMemoKey + "/"
                var childUpdates: Map<String, Any?> = model.toMap(path)

                mDatabaseReference.updateChildren(childUpdates)

                finish()
            }
        }
    }
}