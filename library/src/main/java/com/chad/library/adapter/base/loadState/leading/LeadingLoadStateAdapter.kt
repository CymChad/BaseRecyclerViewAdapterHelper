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
     * A listener for loading more.
     * 加载更多的监听事件
     */
    var onLeadingListener: OnLeadingListener? = null
        private set

    /**
     * Whether enable loading.
     * 是否开启加载功能
     */
    var isLoadEnable = true

    /**
     * Preload, the number of distances from the first item.
     *
     * 预加载，距离首 item 的个数
     */
    var preloadSize = 0

    private var mDelayNextLoadFlag: Boolean = false

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading
    }

    @CallSuper
    override fun onViewAttachedToWindow(holder: VH) {
        loadAction()
    }

    /**
     * Action of loading more.
     * 加载更多执行的操作
     */
    private fun loadAction() {
        if (!isLoadEnable || onLeadingListener?.isAllowLoading() == false) return

        if (mDelayNextLoadFlag) return

        if (loadState is LoadState.NotLoading && !loadState.endOfPaginationReached) {
            val recyclerView = recyclerView ?: return

            if (recyclerView.isComputingLayout) {
                // 如果 RecyclerView 当前正在计算布局，则延迟执行，避免崩溃
                // To avoid crash. Delay to load more if the recyclerview is computingLayout.
                mDelayNextLoadFlag = true
                recyclerView.post {
                    mDelayNextLoadFlag = false
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

    override fun toString(): String {
        return """
            LeadingLoadStateAdapter ->
            [isLoadEnable: $isLoadEnable],
            [preloadSize: $preloadSize],
            [loadState: $loadState]
        """.trimIndent()
    }

    interface OnLeadingListener {

        /**
         * Executing loading.
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