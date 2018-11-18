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
        var view: MemoView
        var model: MemoModel
        var mDatabaseReference: DatabaseReference
        var title: String
        var content: String

        fun sendData()
        fun SaveMemo(MemoKey: String?, context: Context,resources: Resources)
    }
}