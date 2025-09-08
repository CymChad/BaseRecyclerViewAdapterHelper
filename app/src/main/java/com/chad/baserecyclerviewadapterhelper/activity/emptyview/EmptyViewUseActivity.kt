package com.chad.baserecyclerviewadapterhelper.activity.emptyview

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.activity.emptyview.adapter.EmptyViewAdapter
import com.chad.baserecyclerviewadapterhelper.base.BaseViewBindingActivity
import com.chad.baserecyclerviewadapterhelper.data.DataServer
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityEmptyViewUseBinding

class EmptyViewUseActivity : BaseViewBindingActivity<ActivityEmptyViewUseBinding>() {

    private val mAdapter = EmptyViewAdapter()

    private var mError = true
    private var mNoData = true

    override fun initBinding(): ActivityEmptyViewUseBinding =
        ActivityEmptyViewUseBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.titleBar) { view, insets ->
            val bar = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = bar.top)
            insets
        }

        viewBinding.titleBar.title = "EmptyView Use"
        viewBinding.titleBar.setOnBackListener { finish() }

        viewBinding.btnReset.setOnClickListener { reset() }

        viewBinding.rvList.adapter = mAdapter
        viewBinding.rvList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // 打开空布局功能
        mAdapter.isStateViewEnable = true
        mAdapter.isUseStateViewSize = true

        onRefresh()
    }

    private fun reset() {
        mError = true
        mNoData = true
        mAdapter.submitList(null)
        onRefresh()
    }

    private val emptyDataView: View
        get() {
            val notDataView = layoutInflater.inflate(R.layout.empty_view, FrameLayout(this), false)
            notDataView.setOnClickListener { onRefresh() }
            return notDataView
        }

    private val errorView: View
        get() {
            val errorView = layoutInflater.inflate(R.layout.error_view, FrameLayout(this), false)
            errorView.setOnClickListener { onRefresh() }
            return errorView
        }

    private fun onRefresh() {
        // 方式一：直接传入 layout id
        mAdapter.setStateViewLayout(this, R.layout.loading_view)

        viewBinding.rvList.postDelayed({
            if (mError) { // 模拟网络错误
                // 方式二：传入View
                mAdapter.stateView = errorView

                mError = false
            } else {
                if (mNoData) { // 模拟接口没有数据
                    mAdapter.stateView = emptyDataView
                    mNoData = false
                } else {
                    // 模拟正常数据返回
                    mAdapter.submitList(DataServer.getSampleData(10))
                }
            }
        }, 1000)
    }
}