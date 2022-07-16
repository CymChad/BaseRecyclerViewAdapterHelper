package com.chad.library.adapter.base.loadState.trailing

interface OnLoadMoreListener {

    /**
     * 是否允许进行"加载更多"
     */
    fun isCanLoadMore(): Boolean

    /**
     * "加载更多"执行逻辑
     */
    fun loadMore()

    /**
     * 失败的情况下，点击重试执行的逻辑
     */
    fun failRetry()
}