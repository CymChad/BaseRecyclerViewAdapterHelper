package com.allen.kotlinapp

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.allen.kotlinapp.adapter.MultipleItemQuickAdapter
import com.allen.kotlinapp.base.BaseActivity
import com.allen.kotlinapp.data.DataServer
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * 文 件 名: MultipleItemUseActivity
 * 创 建 人: Allen
 * 创建日期: 2017/6/20 11:22
 * 修改时间：
 * 修改备注：
 */
class MultipleItemUseActivity : BaseActivity() {
    private var mRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple_item_use)
        setTitle("MultipleItem Use")
        setBackBtn()
        mRecyclerView = findViewById(R.id.rv_list) as RecyclerView
        val data = DataServer.getMultipleItemData()
        val multipleItemAdapter = MultipleItemQuickAdapter(this, data)
        val manager = GridLayoutManager(this, 4)
        mRecyclerView?.layoutManager = manager
        multipleItemAdapter.setSpanSizeLookup(BaseQuickAdapter.SpanSizeLookup { gridLayoutManager, position -> data.get(position).spanSize })
        mRecyclerView?.adapter = multipleItemAdapter
    }


}
