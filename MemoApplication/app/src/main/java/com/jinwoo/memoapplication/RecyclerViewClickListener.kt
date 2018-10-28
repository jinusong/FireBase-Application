package com.jinwoo.memoapplication

import com.jinwoo.memoapplication.model.MemoModel

interface RecyclerViewClickListener{
    fun onItemClicked(position: Int, memo: MemoModel, key: String)
    fun onItemLongClicked(position: Int, memo: MemoModel, key: String)
}