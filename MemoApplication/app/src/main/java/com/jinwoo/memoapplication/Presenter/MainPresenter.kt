package com.jinwoo.memoapplication.Presenter

import android.content.Context
import android.content.res.Resources
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jinwoo.memoapplication.Contract.MainContract
import com.jinwoo.memoapplication.Model.adapter.MemoAdapter
import com.jinwoo.memoapplication.R

class MainPresenter(view: MainContract.MainView) : MainContract.MainPresenter {
    override lateinit var view: MainContract.MainView
    override lateinit var mDatabaseReference: DatabaseReference
    override lateinit var mAdapter: MemoAdapter

    init {
        this.view = view
    }

    override fun setRecyclerView(recyclerView: RecyclerView, context: Context): RecyclerView{
        var decoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(decoration)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return recyclerView
    }

    override fun initData(resources: Resources): MemoAdapter{
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(resources.getString(R.string.notechild))
        mAdapter = MemoAdapter(mDatabaseReference)
        return mAdapter
    }
}