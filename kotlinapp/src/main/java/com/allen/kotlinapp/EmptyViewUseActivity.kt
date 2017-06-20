package com.allen.kotlinapp

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.allen.kotlinapp.adapter.QuickAdapter
import com.allen.kotlinapp.base.BaseActivity
import com.allen.kotlinapp.data.DataServer

/**
 * 文 件 名: EmptyViewUseActivity
 * 创 建 人: Allen
 * 创建日期: 2017/6/15 10:34
 * 修改时间：
 * 修改备注：
 */
class EmptyViewUseActivity : BaseActivity(), View.OnClickListener {
    private var mRecyclerView: RecyclerView? = null
    private var mQuickAdapter: QuickAdapter? = null
    private var notDataView: View? = null
    private var errorView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackBtn()
        setTitle("EmptyView Use")
        setContentView(R.layout.activity_empty_view_use)
        findViewById(R.id.btn_reset).setOnClickListener(this)
        mRecyclerView = findViewById(R.id.rv_list) as RecyclerView
        mRecyclerView?.setHasFixedSize(true)
        mRecyclerView?.layoutManager = LinearLayoutManager(this)

        notDataView = layoutInflater.inflate(R.layout.empty_view, mRecyclerView?.parent as ViewGroup, false)
        notDataView?.setOnClickListener { onRefresh() }
        errorView = layoutInflater.inflate(R.layout.error_view, mRecyclerView?.parent as ViewGroup, false)
        errorView?.setOnClickListener { onRefresh() }
        initAdapter()
        onRefresh()
    }

    private fun initAdapter() {
        mQuickAdapter = QuickAdapter(0)
        mRecyclerView?.adapter = mQuickAdapter
    }

    override fun onClick(v: View) {
        mError = true
        mNoData = true
        mQuickAdapter?.setNewData(null)
        onRefresh()
    }

    private var mError = true
    private var mNoData = true

    private fun onRefresh() {
        mQuickAdapter?.setEmptyView(R.layout.loading_view, mRecyclerView?.parent as ViewGroup)
        Handler().postDelayed({
            if (mError) {
                mQuickAdapter?.emptyView = errorView
                mError = false
            } else {
                if (mNoData) {
                    mQuickAdapter?.emptyView = notDataView
                    mNoData = false
                } else {
                    mQuickAdapter?.setNewData(DataServer.getSampleData(10))
                }
            }
        }, 1000)
    }
}

