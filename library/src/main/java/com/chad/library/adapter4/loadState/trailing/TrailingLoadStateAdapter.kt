package com.chad.library.adapter4.loadState.trailing

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.LoadStateAdapter

/**
 * Tail load more parent class Adapter.
 * 尾部加载更多的父类 Adapter
 *
 * Custom layout: You can modify the layout by extends this class and customizing [RecyclerView.ViewHolder].
 * 自定义布局：可以通过继承此类，并自定义[RecyclerView.ViewHolder]来修改布局
 */
abstract class TrailingLoadStateAdapter<VH : RecyclerView.ViewHolder>(
    /**
     * Whether to display "Loading end" after all data is loaded.
     *
     * 所有数据加载完毕后，是否显示"加载结束"，必须初始化时传递，中途无法修改参数。
     */
    val isLoadEndDisplay: Boolean = true
) : LoadStateAdapter<VH>() {

    /**
     * Trailing load state listener events
     *
     * 加载更多的监听事件
     */
    var onTrailingListener: OnTrailingListener? = null
        private set

    /**
     * Whether to turn on autoload more. Called when it must be initialized
     *
     * 是否打开自动加载更多
     */
    var isAutoLoadMore = true

    /**
     * Preload, the number of items from the tail.
     *
     * 预加载，距离尾部 item 的个数
     */
    var preloadSize = 0

    /**
     * A flag to determine if you can load when content doesn't fill the screen.
     * 不满一屏时，是否可以继续加载的标记位
     */
    private var mNotFullPageNextLoadFlag: Boolean = false

    private var mDelayNextLoadFlag: Boolean = false

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return super.displayLoadStateAsItem(loadState)
                || (loadState is LoadState.NotLoading && !loadState.endOfPaginationReached) // 加载完成的状态，并且还有分页数据的时候，需要显示
                || (isLoadEndDisplay && (loadState is LoadState.NotLoading && loadState.endOfPaginationReached)) // 加载彻底结束，不会再有分页数据的情况，并且需要显示结束后的item
    }

    @CallSuper
    override fun onViewAttachedToWindow(holder: VH) {
        loadAction()
    }

    /**
     * Executing loading.
     * 执行加载的操作
     */
    private fun loadAction() {
        if (!isAutoLoadMore || onTrailingListener?.isAllowLoading() == false) {
            // 不允许进行加载更多（例如：正在进行下拉刷新）
            // Loading more is forbidden at the moment.(eg: when pulling down to refresh)
            return
        }

        if (mNotFullPageNextLoadFlag || mDelayNextLoadFlag) {
            return
        }

        if (loadState is LoadState.NotLoading && !loadState.endOfPaginationReached) {
            val recyclerView = recyclerView ?: return

            if (recyclerView.isComputingLayout) {
                // 如果 RecyclerView 当前正在计算布局，则延迟执行，避免崩溃
                // To avoid crash. Delay to load more if the recyclerview is computingLayout.
                mDelayNextLoadFlag = true
                recyclerView.post {
                    mDelayNextLoadFlag = false
                    invokeLoadMore()
                }
                return
            }

            invokeLoadMore()
        }
    }

    internal fun checkPreload(itemCount: Int, currentPosition: Int) {
        if (currentPosition > itemCount - 1) return

        if (itemCount - currentPosition - 1 <= preloadSize) {
            loadAction()
        }
    }

    fun invokeLoadMore() {
        loadState = LoadState.Loading
        onTrailingListener?.onLoad()
    }

    fun invokeFailRetry() {
        loadState = LoadState.Loading
        onTrailingListener?.onFailRetry()
    }

    /**
     * Call this method, Check disable load more if not full page.
     * 调用此方法，当数据不满足一屏幕的时候，暂停加载更多。应在提交数据 notify 后马上调用。
     */
    fun checkDisableLoadMoreIfNotFullPage() {
        // 先把标记位设置为false
        mNotFullPageNextLoadFlag = true
        val recyclerView = this.recyclerView ?: return
        val manager = recyclerView.layoutManager ?: return

        if (manager is LinearLayoutManager) {
            recyclerView.post {
                if (isFullScreen()) {
                    mNotFullPageNextLoadFlag = false
                }
            }
        } else if (manager is StaggeredGridLayoutManager) {
            recyclerView.post {
                val positions = IntArray(manager.spanCount)
                manager.findLastCompletelyVisibleItemPositions(positions)
                val pos = getTheBiggestNumber(positions) + 1
                if (pos != recyclerView.adapter?.itemCount) {
                    mNotFullPageNextLoadFlag = false
                }
            }
        }
    }

    private fun isFullScreen(): Boolean {
        val adapter = recyclerView?.adapter ?: return true
        val llm = recyclerView?.layoutManager  as? LinearLayoutManager ?: return true

        return (llm.findLastCompletelyVisibleItemPosition() + 1) != adapter.itemCount ||
                llm.findFirstCompletelyVisibleItemPosition() != 0
    }

    private fun getTheBiggestNumber(numbers: IntArray?): Int {
        var tmp = -1
        if (numbers == null || numbers.isEmpty()) {
            return tmp
        }
        for (num in numbers) {
            if (num > tmp) {
                tmp = num
            }
        }
        return tmp
    }

    /**
     * Set load state listener
     * 设置监听事件
     * @param listener
     */
    fun setOnLoadMoreListener(listener: OnTrailingListener?) = apply {
        this.onTrailingListener = listener
    }

    override fun toString(): String {
        return """
            TrailingLoadStateAdapter ->
            [isLoadEndDisplay: $isLoadEndDisplay],
            [isAutoLoadMore: $isAutoLoadMore],
            [preloadSize: $preloadSize],
            [loadState: $loadState]
        """.trimIndent()
    }

    interface OnTrailingListener {

        /**
         * "加载更多"执行的逻辑
         */
        fun onLoad()

        /**
         * 失败的情况下，点击重试执行的逻辑
         */
        fun onFailRetry()

        /**
         * Whether to allow loading.
         * 是否允许进行加载更多（例如下拉刷新时，就不应该进行加载更多）
         */
        fun isAllowLoading(): Boolean = true
    }
}