package com.jinwoo.memoapplication.Presenter

import android.content.Context
import android.content.res.Resources
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jinwoo.memoapplication.Contract.MainContract
import com.jinwoo.memoapplication.Model.MemoModel
import com.jinwoo.memoapplication.Model.adapter.MemoAdapter
import com.jinwoo.memoapplication.R
import com.jinwoo.memoapplication.RecyclerViewClickListener
import com.jinwoo.memoapplication.View.MemoActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity

class MainPresenter(view: MainContract.MainView) : MainContract.MainPresenter {
    override val view: MainContract.MainView
    override val mDatabaseReference: DatabaseReference by lazy { FirebaseDatabase.getInstance().getReference("notes") }
    override val mAdapter: MemoAdapter by lazy { MemoAdapter(mDatabaseReference) }

    init {
        this.view = view
    }
    override fun setRecyclerView(recyclerView: RecyclerView, context: Context): RecyclerView{
        var decoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(decoration)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return recyclerView
    }

    override fun getAdapter(): MemoAdapter = mAdapter
}