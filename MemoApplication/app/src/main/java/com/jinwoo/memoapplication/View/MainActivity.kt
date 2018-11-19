package com.jinwoo.memoapplication.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.jinwoo.memoapplication.Contract.MainContract
import com.jinwoo.memoapplication.Presenter.MainPresenter
import com.jinwoo.memoapplication.Model.MemoModel
import com.jinwoo.memoapplication.R
import com.jinwoo.memoapplication.RecyclerViewClickListener
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), MainContract.MainView {

    lateinit var mainPresenter: MainContract.MainPresenter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainPresenter = MainPresenter(this)

        recyclerView = findViewById(R.id.recycler_list)
        recyclerView = mainPresenter.setRecyclerView(recyclerView, this)
        recyclerView.adapter = mainPresenter.initData(resources)

        main_fab_add.setOnClickListener { startActivity<MemoActivity>() }
    }

    override fun createAlert(key: String) =
            alert(title = "메모 삭제", message = "메모를 삭제하시겠습니까?") {
            positiveButton("삭제") {
                mainPresenter.mDatabaseReference.child(key).removeValue()
            }
            negativeButton("취소") { null }
        }

    override fun enterMemo(memo: MemoModel, key: String){
        startActivity<MemoActivity>("memokey" to key,
                "title" to memo.title, "contents" to memo.contents)
    }
}
