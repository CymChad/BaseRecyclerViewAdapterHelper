package com.allen.kotlinapp.loadmore

import com.allen.kotlinapp.R
import com.chad.library.adapter.base.loadmore.LoadMoreView

/**
 * 文 件 名: CustomLoadMoreView
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 15:15
 * 修改时间：
 * 修改备注：
 */
class CustomLoadMoreView : LoadMoreView() {

    override fun getLayoutId(): Int {
        return R.layout.view_load_more
    }

    override fun getLoadingViewId(): Int {
        return R.id.load_more_loading_view
    }

    override fun getLoadFailViewId(): Int {
        return R.id.load_more_load_fail_view
    }

    override fun getLoadEndViewId(): Int {
        return R.id.load_more_load_end_view
    }
}
