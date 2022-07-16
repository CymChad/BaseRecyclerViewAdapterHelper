package com.chad.library.adapter.base.loadState.trailing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.chad.library.R
import com.chad.library.adapter.base.loadState.LoadState
import com.chad.library.adapter.base.loadState.LoadStateAdapter
import com.chad.library.adapter.base.loadState.OnLoadMoreListener
import com.chad.library.databinding.BrvahTrailingLoadMoreBinding

/**
 * 尾部加载更多 Adapter
 *
 * 自定义布局：可以通过继承此类，并自定义[TrailingLoadStateAdapter.TrailingViewHolder]来修改布局
 *
 */
open class TrailingLoadStateAdapter : LoadStateAdapter<TrailingLoadStateAdapter.TrailingViewHolder>() {

    /**
     * 加载更多的事件
     */
    private var loadMoreListener: OnLoadMoreListener? = null

    /**
     * 不满一屏时，是否可以继续加载的标记位
     */
    private var mNextLoadEnable: Boolean = true

    /**
     * 所有数据加载完毕后，是否显示"加载结束"
     */
    var isLoadEndDisplay: Boolean = true

    /**
     * 是否打开自动加载更多
     */
    var isAutoLoadMore = true

    /**
     * 当自动加载更多开启，并且数据不满一屏时，是否关闭自动加载更多。默认为 false
     */
    var isDisableLoadMoreIfNotFullPage = false

    /**
     * 不可以重写
     */
    final override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): TrailingViewHolder =
        onCreateViewHolder(parent.context, parent, loadState).apply {
            getLoadFailView(itemView).setOnClickListener {
                // 失败重试点击事件
                loadMoreListener?.failRetry()
            }
            getLoadComplete(itemView).setOnClickListener {
                // 加载更多，手动点击事件
                loadMoreListener?.loadMore()
            }
        }

    final override fun onBindViewHolder(holder: TrailingViewHolder, loadState: LoadState) =
        holder.onBind(loadState)

    override fun getStateViewType(loadState: LoadState): Int = R.layout.brvah_trailing_load_more

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return if (isLoadEndDisplay) {
            // 加载完毕，也显示状态Item
            super.displayLoadStateAsItem(loadState) || (loadState is LoadState.NotLoading && loadState.endOfPaginationReached)
        } else {
            super.displayLoadStateAsItem(loadState)
        }
    }

    @CallSuper
    override fun onViewAttachedToWindow(holder: TrailingViewHolder) {
        loadMoreAction()
    }

    /**
     * 可以重写此方法，返回自定义的 [TrailingViewHolder]
     */
    open fun onCreateViewHolder(context: Context, parent: ViewGroup, loadState: LoadState) : TrailingViewHolder {
        return TrailingLoadStateVH(parent)
    }

    /**
     * 加载更多执行的操作
     */
    @Synchronized
    private fun loadMoreAction() {
        if (!isAutoLoadMore || loadMoreListener?.isCanLoadMore() == false) {
            // 不允许进行加载更多（例如：正在进行下拉刷新）
            return
        }

        if (!mNextLoadEnable) {
            return
        }

        if (loadState is LoadState.NotLoading && !loadState.endOfPaginationReached) {
            val recyclerView = recyclerView ?: return

            if (recyclerView.isComputingLayout) {
                // 如果 RecyclerView 当前正在计算布局，则延迟执行，避免崩溃
                recyclerView.post {
                    loadMoreListener?.loadMore()
                }
                return
            }
            loadMoreListener?.loadMore()
        }
    }

    fun checkDisableLoadMoreIfNotFullPage() {
        if (!isDisableLoadMoreIfNotFullPage) {
            return
        }
        // 先把标记位设置为false
        mNextLoadEnable = false
        val recyclerView =  this.recyclerView ?: return
        val manager = recyclerView.layoutManager ?: return
        if (manager is LinearLayoutManager) {
            recyclerView.post {
                if (isFullScreen(manager)) {
                    mNextLoadEnable = true
                }
            }
        } else if (manager is StaggeredGridLayoutManager) {
            recyclerView.post {
                // TODO
//                val positions = IntArray(manager.spanCount)
//                manager.findLastCompletelyVisibleItemPositions(positions)
//                val pos = getTheBiggestNumber(positions) + 1
//                if (pos != baseQuickAdapter.itemCount) {
//                    mNextLoadEnable = true
//                }
            }
        }
    }

    private fun isFullScreen(llm: LinearLayoutManager): Boolean {
        val adapter = recyclerView?.adapter ?: return true

        return (llm.findLastCompletelyVisibleItemPosition() + 1) != adapter.itemCount ||
                llm.findFirstCompletelyVisibleItemPosition() != 0
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener?) {
        this.loadMoreListener = onLoadMoreListener
    }

    /**
     * viewBinding 方式创建 "加载更多" 的 ViewHolder
     * @param V : ViewBinding
     * @property viewBinding V
     * @constructor
     */
    abstract class TrailingViewBindingVH<V : ViewBinding>(
        private val viewBinding: V,
    ) : TrailingViewHolder(viewBinding.root) {
        final override fun getLoadComplete(itemView: View): View = getLoadComplete(viewBinding)

        final override fun getLoadingView(itemView: View): View = getLoadingView(viewBinding)

        final override fun getLoadEndView(itemView: View): View = getLoadEndView(viewBinding)

        final override fun getLoadFailView(itemView: View): View = getLoadFailView(viewBinding)

        abstract fun getLoadComplete(viewBinding: V): View

        abstract fun getLoadingView(viewBinding: V): View

        abstract fun getLoadEndView(viewBinding: V): View

        abstract fun getLoadFailView(viewBinding: V): View
    }

    /**
     * 基类，创建 "加载更多" 的 ViewHolder
     */
    abstract class TrailingViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            val layoutParams = this.itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        }

        /**
         * 布局中的 加载更多视图
         */
        abstract fun getLoadingView(itemView: View): View

        /**
         * 布局中的 加载完成布局
         */
        abstract fun getLoadComplete(itemView: View): View

        /**
         * 布局中的 加载结束布局
         */
        abstract fun getLoadEndView(itemView: View): View

        /**
         * 布局中的 加载失败布局
         */
        abstract fun getLoadFailView(itemView: View): View

        /**
         * 可以重写此方法，实行自定义逻辑
         * @param loadState LoadState
         */
        open fun onBind(loadState: LoadState) {
            when (loadState) {
                is LoadState.NotLoading -> {
                    if (loadState.endOfPaginationReached) {
                        getLoadComplete(itemView).visibility = View.GONE
                        getLoadingView(itemView).visibility = View.GONE
                        getLoadFailView(itemView).visibility = View.GONE
                        getLoadEndView(itemView).visibility = View.VISIBLE
                    } else {
                        getLoadComplete(itemView).visibility = View.VISIBLE
                        getLoadingView(itemView).visibility = View.GONE
                        getLoadFailView(itemView).visibility = View.GONE
                        getLoadEndView(itemView).visibility = View.GONE
                    }
                }
                is LoadState.Loading -> {
                    getLoadComplete(itemView).visibility = View.GONE
                    getLoadingView(itemView).visibility = View.VISIBLE
                    getLoadFailView(itemView).visibility = View.GONE
                    getLoadEndView(itemView).visibility = View.GONE
                }
                is LoadState.Error -> {
                    getLoadComplete(itemView).visibility = View.GONE
                    getLoadingView(itemView).visibility = View.GONE
                    getLoadFailView(itemView).visibility = View.VISIBLE
                    getLoadEndView(itemView).visibility = View.GONE
                }
            }
        }
    }

    /**
     * 继承基类，实现默认的"加载更多"VH
     */
    private class TrailingLoadStateVH(
        parent: ViewGroup,
        viewBinding: BrvahTrailingLoadMoreBinding = BrvahTrailingLoadMoreBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : TrailingLoadStateAdapter.TrailingViewBindingVH<BrvahTrailingLoadMoreBinding>(
        viewBinding
    ) {

        override fun getLoadComplete(viewBinding: BrvahTrailingLoadMoreBinding): View {
            return viewBinding.loadMoreLoadCompleteView
        }

        override fun getLoadingView(viewBinding: BrvahTrailingLoadMoreBinding): View {
            return viewBinding.loadMoreLoadingView
        }

        override fun getLoadEndView(viewBinding: BrvahTrailingLoadMoreBinding): View {
            return viewBinding.loadMoreLoadEndView
        }

        override fun getLoadFailView(viewBinding: BrvahTrailingLoadMoreBinding): View {
            return viewBinding.loadMoreLoadFailView
        }
    }
}