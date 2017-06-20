package com.allen.kotlinapp

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.allen.kotlinapp.adapter.HeaderAndFooterAdapter
import com.allen.kotlinapp.base.BaseActivity
import com.allen.kotlinapp.data.DataServer
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * 文 件 名: HeaderAndFooterUseActivity
 * 创 建 人: Allen
 * 创建日期: 2017/6/20 11:14
 * 修改时间：
 * 修改备注：
 */
class HeaderAndFooterUseActivity : BaseActivity() {

    private var mRecyclerView: RecyclerView? = null
    private var headerAndFooterAdapter: HeaderAndFooterAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackBtn()
        setTitle("HeaderAndFooter Use")

        setContentView(R.layout.activity_header_and_footer_use)
        mRecyclerView = findViewById(R.id.rv_list) as RecyclerView
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
        initAdapter()

        val headerView = getHeaderView(0, View.OnClickListener { headerAndFooterAdapter?.addHeaderView(getHeaderView(1, removeHeaderListener), 0) })
        headerAndFooterAdapter?.addHeaderView(headerView)


        val footerView = getFooterView(0, View.OnClickListener { headerAndFooterAdapter?.addFooterView(getFooterView(1, removeFooterListener), 0) })
        headerAndFooterAdapter?.addFooterView(footerView, 0)

        mRecyclerView?.adapter = headerAndFooterAdapter

    }


    private fun getHeaderView(type: Int, listener: View.OnClickListener): View {
        val view: View = layoutInflater.inflate(R.layout.head_view, mRecyclerView?.parent as ViewGroup, false)
        if (type == 1) {
            (view.findViewById(R.id.iv) as ImageView).setImageResource(R.mipmap.rm_icon)
        }
        view.setOnClickListener(listener)
        return view
    }

    private fun getFooterView(type: Int, listener: View.OnClickListener): View {
        val view = getLayoutInflater().inflate(R.layout.footer_view, mRecyclerView?.parent as ViewGroup, false)
        if (type == 1) {
            (view.findViewById(R.id.iv) as ImageView).setImageResource(R.mipmap.rm_icon)
        }
        view.setOnClickListener(listener)
        return view
    }

    private val removeHeaderListener: View.OnClickListener
        get() = View.OnClickListener { v -> headerAndFooterAdapter?.removeHeaderView(v) }


    private val removeFooterListener: View.OnClickListener
        get() = View.OnClickListener { v -> headerAndFooterAdapter?.removeFooterView(v) }

    private fun initAdapter() {
        headerAndFooterAdapter = HeaderAndFooterAdapter(PAGE_SIZE)
        headerAndFooterAdapter?.openLoadAnimation()
        mRecyclerView?.adapter = headerAndFooterAdapter
        //        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
        //            @Override
        //            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
        //                Toast.makeText(HeaderAndFooterUseActivity.this, "" + Integer.toString(position), Toast.LENGTH_LONG).show();
        //            }
        //        });
        headerAndFooterAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            adapter.setNewData(DataServer.getSampleData(PAGE_SIZE))
            Toast.makeText(this@HeaderAndFooterUseActivity, "" + Integer.toString(position), Toast.LENGTH_LONG).show()
        }

    }

    companion object {
        private val PAGE_SIZE = 3
    }

}
