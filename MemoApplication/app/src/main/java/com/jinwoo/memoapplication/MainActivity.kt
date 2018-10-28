package com.jinwoo.memoapplication

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jinwoo.memoapplication.adapter.MemoAdapter
import com.jinwoo.memoapplication.model.MemoModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mAdapter: MemoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var decoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        mainactivity_recycler_list.addItemDecoration(decoration)

        mainactivity_recycler_list.layoutManager = LinearLayoutManager(this)

        mDatabaseReference = FirebaseDatabase.getInstance().getReference(resources.getString(R.string.notechild))

        mAdapter = MemoAdapter(mDatabaseReference)

        mainactivity_recycler_list.adapter = mAdapter

        mainactivity_fab_add.setOnClickListener {
            var intent = Intent(this@MainActivity, MemoActivity::class.java)
            startActivity(intent)
        }

        mAdapter.setOnClickListener(object: RecyclerViewClickListener{
            override fun onItemClicked(position: Int, memo: MemoModel, key: String) {
                var intent = Intent(this@MainActivity, MemoActivity::class.java)

                intent.putExtra("memokey", key)
                intent.putExtra("title", memo.title)
                intent.putExtra("contents", memo.contents)

                startActivity(intent)
            }

            override fun onItemLongClicked(position: Int, memo: MemoModel, key: String) {
                val memoKey = key
                var builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("메모 삭제")
                builder.setMessage("메모를 삭제하시겠습니까?")
                builder.setPositiveButton("삭제") { dialogInterface: DialogInterface, i: Int ->
                    mDatabaseReference.child(memoKey).removeValue()
                }
                builder.setNegativeButton("취소", null)
                builder.show()
            }
        })
    }
}
