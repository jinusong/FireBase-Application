package com.jinwoo.memoapplication.Contract

import android.content.Context
import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.jinwoo.memoapplication.Model.MemoModel
import com.jinwoo.memoapplication.Model.adapter.MemoAdapter
import org.jetbrains.anko.AlertBuilder

interface MainContract{
    interface MainView{
        fun createAlert(key: String): AlertBuilder<android.app.AlertDialog>
        fun enterMemo(memo: MemoModel, key: String)
    }
    interface MainPresenter{
        var view: MainView
        var mDatabaseReference: DatabaseReference
        var mAdapter: MemoAdapter

        fun setRecyclerView(recyclerView: RecyclerView, context: Context): RecyclerView
        fun initData(resources: Resources): MemoAdapter
        fun listClickListener()
    }
}