package com.jinwoo.memoapplication.Contract

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import com.google.firebase.database.DatabaseReference
import com.jinwoo.memoapplication.Model.MemoModel

interface MemoContract{
    interface MemoView{
        fun notifyfinish()
        fun notifyError(error: String)
        fun setMemo(intent: Intent)
    }
    interface MemoPresenter{
        val view: MemoView
        var model: MemoModel
        val mDatabaseReference: DatabaseReference

        fun sendData(title: String, content: String)
        fun keyNullCheck(MemoKey: String?, context: Context)
        fun SaveMemo(context: Context)
        fun UpdateMemo(MemoKey: String?)
    }
}