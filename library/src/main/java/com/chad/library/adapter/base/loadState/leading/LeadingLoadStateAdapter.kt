package com.chad.library.adapter.base.loadState.leading

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.loadState.LoadState
import com.chad.library.adapter.base.loadState.LoadStateAdapter

/**
 * Leading load state adapter
 * 首部的加载状态适配器
 */
abstract class LeadingLoadStateAdapter<VH: RecyclerView.ViewHolder> : LoadStateAdapter<VH>() {

    /**
     * 加载更多的监听事件
     */
    var onLeadingListener: OnLeadingListener? = null
        private set

    /**
     * 是否开启加载功能
     */
    var isLoadEnable = true

    /**
     * 预加载，距离首 item 的个数
     */
    var preloadSize = 0

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading
    }

    @CallSuper
    override fun onViewAttachedToWindow(holder: VH) {
        loadAction()
    }

    /**
     * 加载更多执行的操作
     */
    private fun loadAction() {
        if (!isLoadEnable || onLeadingListener?.isAllowLoading() == false) return

        if (loadState is LoadState.NotLoading && !loadState.endOfPaginationReached) {
            val recyclerView = recyclerView ?: return

            if (recyclerView.isComputingLayout) {
                // 如果 RecyclerView 当前正在计算布局，则延迟执行，避免崩溃
                recyclerView.post {
                    invokeLoad()
                }
                return
            }
            invokeLoad()
        }
    }

    internal fun checkPreload(currentPosition: Int) {
        if (currentPosition < 0) return

        if (currentPosition <= preloadSize) {
            loadAction()
        }
    }

    fun invokeLoad() {
        loadState = LoadState.Loading
        onLeadingListener?.onLoad()
    }


    fun setOnLeadingListener(listener: OnLeadingListener?) = apply {
        this.onLeadingListener = listener
    }

    interface OnLeadingListener {

        /**
         * "加载更多"执行逻辑
         */
        fun onLoad()

        /**
         * Whether to allow loading.
         * 是否允许进行加载
         */
        fun isAllowLoading(): Boolean = true
    }
}