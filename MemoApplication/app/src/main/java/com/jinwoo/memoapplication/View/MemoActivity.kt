package com.jinwoo.memoapplication.View

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.jinwoo.memoapplication.Contract.MemoContract
import com.jinwoo.memoapplication.Presenter.MemoPresenter
import com.jinwoo.memoapplication.R
import kotlinx.android.synthetic.main.activity_memo.*

class MemoActivity:AppCompatActivity(), MemoContract.MemoView {

    lateinit var memoPresenter: MemoContract.MemoPresenter
    var mMemoKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        memoPresenter = MemoPresenter(this)

        intent?.let{ setMemo(intent) }

        memoPresenter.mDatabaseReference = FirebaseDatabase.getInstance().getReference()

        memo_btn_save.setOnClickListener {
            memoPresenter.title = memo_title.text.toString()
            memoPresenter.content = memo_content.text.toString()

            memoPresenter.sendData()
            memoPresenter.SaveMemo(mMemoKey, this, resources)
        }
    }

    override fun notifyfinish() = finish()

    override fun notifyError(error: String) = memo_title.setError(error)

    override fun setMemo(intent: Intent) {
        mMemoKey = intent.getStringExtra("memokey")
        var title: String? = intent.getStringExtra("title")
        var contents: String? = intent.getStringExtra("contents")
        memo_title.setText(title)
        memo_content.setText(contents)
    }
}