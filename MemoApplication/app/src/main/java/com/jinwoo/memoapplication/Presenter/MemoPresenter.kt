package com.jinwoo.memoapplication.Presenter

import android.content.Context
import android.content.res.Resources
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.jinwoo.memoapplication.Contract.MemoContract
import com.jinwoo.memoapplication.Model.MemoModel
import com.jinwoo.memoapplication.R
import java.text.SimpleDateFormat
import java.util.*

class MemoPresenter(view: MemoContract.MemoView) : MemoContract.MemoPresenter{

    override lateinit var view: MemoContract.MemoView
    override lateinit var model: MemoModel
    override lateinit var mDatabaseReference: DatabaseReference
    override lateinit var title: String
    override lateinit var content: String

    private val REQUIRED_TITLE: String = "제목을 입력하세요."
    private val REQUIRED_CONTENTS: String = "내용을 입력하세요."

    init {
        this.view = view
    }

    override fun sendData() {
        var dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm")
        model = MemoModel(title, content, dateFormat.format(Date()))
    }

    override fun SaveMemo(MemoKey: String?, context: Context, resources: Resources) {
        MemoKey.let{
            if (TextUtils.isEmpty(title)) {
                view.notifyError(REQUIRED_TITLE)
                return
            }
            if (TextUtils.isEmpty(content)) {
                view.notifyError(REQUIRED_CONTENTS)
                return
            }
            mDatabaseReference.child(resources.getString(R.string.notechild))
                    .push().setValue(model).addOnSuccessListener {
                        Toast.makeText(context, "저장완료", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(context, "저장실패", Toast.LENGTH_SHORT).show()
                    }
            view.notifyfinish()
        } .let {
            var path: String = resources.getString(R.string.notechild) + "/" + MemoKey + "/"
            var childUpdates: Map<String, Any?> = model.toMap(path)

            mDatabaseReference.updateChildren(childUpdates)

            view.notifyfinish()
        }
    }

}