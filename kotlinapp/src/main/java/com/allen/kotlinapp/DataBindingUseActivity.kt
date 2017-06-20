package com.allen.kotlinapp

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.allen.kotlinapp.adapter.DataBindingUseAdapter
import com.allen.kotlinapp.base.BaseActivity
import com.allen.kotlinapp.entity.Movie
import com.allen.kotlinapp.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import java.util.*

/**
 * 文 件 名: DataBindingUseActivity
 * 创 建 人: Allen
 * 创建日期: 2017/6/20 11:29
 * 修改时间：
 * 修改备注：
 */
class DataBindingUseActivity : BaseActivity() {

    internal var mRecyclerView: RecyclerView? = null
    internal var mAdapter: DataBindingUseAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackBtn()
        setTitle("DataBinding Use")
        setContentView(R.layout.activity_data_binding_use)

        mRecyclerView = findViewById(R.id.rv) as RecyclerView
        mAdapter = DataBindingUseAdapter(R.layout.item_movie, genData())
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
        mRecyclerView?.adapter = mAdapter
        mAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position -> ToastUtils.showShortToast("onItemClick") }
        mAdapter?.onItemChildLongClickListener = BaseQuickAdapter.OnItemChildLongClickListener { adapter, view, position ->
            ToastUtils.showShortToast("onItemChildLongClick")
            true
        }
        mAdapter?.onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position ->
            ToastUtils.showShortToast("onItemLongClick")
            true
        }
    }


    private fun genData(): List<Movie> {
        val list = ArrayList<Movie>()
        val random = Random()
        for (i in 0..9) {
            val name = "Chad"
            val price = random.nextInt(10) + 10
            val len = random.nextInt(80) + 60
            val movie = Movie(name, len, price, "He was one of Australia's most distinguished artistes")
            list.add(movie)
        }
        return list
    }
}
