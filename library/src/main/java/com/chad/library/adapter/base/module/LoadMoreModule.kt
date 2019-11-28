package com.chad.library.adapter.base.module

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
import com.chad.library.adapter.base.loadmore.OnLoadMoreListener
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView

interface LoadMoreImp {
    fun getLoadMoreModule(baseQuickAdapter: BaseQuickAdapter<*, *>): BaseLoadMoreModule {
        return BaseLoadMoreModule(baseQuickAdapter)
    }

}


open class BaseLoadMoreModule(private val baseQuickAdapter: BaseQuickAdapter<*, *>) {

    companion object {
        private var defLoadMoreView: BaseLoadMoreView = SimpleLoadMoreView()

        /**
         * 设置全局的LodeMoreView
         * @param loadMoreView BaseLoadMoreView
         */
        @JvmStatic
        fun setDefLoadMoreView(loadMoreView: BaseLoadMoreView) {
            defLoadMoreView = loadMoreView
        }
    }


    var loadMoreView = defLoadMoreView

    /** 加载完成后是否允许点击 */
    var enableLoadMoreEndClick = false
    /** 是否打开自动加载更多 */
    var isAutoLoadMore = true
    //TODO
    var isEnableLoadMoreIfNotFullPage = true
    var preLoadNumber = 1
        set(value) {
            if (value > 1) {
                field = value
            }
        }
    var loading = false
        internal set


    private var mLoadMoreListener: OnLoadMoreListener? = null
    private var mNextLoadEnable = false



    fun createLoadMoreView(parent: ViewGroup) :View {
        return loadMoreView.getRootView(parent)
    }

    fun setupViewHolder(viewHolder: BaseViewHolder) {
        viewHolder.itemView.setOnClickListener {
            if (loadMoreView.loadMoreStatus == BaseLoadMoreView.Status.Fail) {
                loadMoreToLoading()
            } else if (loadMoreView.loadMoreStatus == BaseLoadMoreView.Status.Complete && !isAutoLoadMore) {
                loadMoreToLoading()
            } else if (enableLoadMoreEndClick && loadMoreView.loadMoreStatus == BaseLoadMoreView.Status.End) {
                loadMoreToLoading()
            }
        }
    }

    /**
     * The notification starts the callback and loads more
     */
    fun loadMoreToLoading() {
        if (loadMoreView.loadMoreStatus == BaseLoadMoreView.Status.Loading) {
            return
        }
        loadMoreView.loadMoreStatus = BaseLoadMoreView.Status.Loading
        baseQuickAdapter.notifyItemChanged(getLoadMoreViewPosition())
        invokeLoadMoreListener()
    }

    /**
     * Gets to load more locations
     *
     * @return
     */
    fun getLoadMoreViewPosition(): Int {
        return baseQuickAdapter.getHeaderLayoutCount() + baseQuickAdapter.data.size + baseQuickAdapter.getFooterLayoutCount()
    }

    var isEnableLoadMore = false
        set(value) {
            val oldHasLoadMore = hasLoadMoreView()
            field = value
            val newHasLoadMore = hasLoadMoreView()

            if (oldHasLoadMore) {
                if (!newHasLoadMore) {
                    baseQuickAdapter.notifyItemRemoved(getLoadMoreViewPosition())
                }
            } else {
                if (newHasLoadMore) {
                    loadMoreView.loadMoreStatus = BaseLoadMoreView.Status.Complete
                    baseQuickAdapter.notifyItemInserted(getLoadMoreViewPosition())
                }
            }
        }

    fun hasLoadMoreView(): Boolean {
        if (mLoadMoreListener == null || !isEnableLoadMore) {
            return false
        }
        if (!mNextLoadEnable && loadMoreView.isLoadEndMoreGone) {
            return false
        }
        return baseQuickAdapter.data.isNotEmpty()
    }

    /**
     * 自定加载数据
     * @param position Int
     */
    internal fun autoLoadMore(position: Int) {
        if (!isAutoLoadMore) {
            //如果不需要自动加载更多，直接返回
            return
        }
        if (!hasLoadMoreView()) {
            return
        }
        if (position < baseQuickAdapter.itemCount - preLoadNumber) {
            return
        }
        if (loadMoreView.loadMoreStatus != BaseLoadMoreView.Status.Complete) {
            return
        }
        invokeLoadMoreListener()
    }

    /**
     * 触发加载更多监听
     */
    private fun invokeLoadMoreListener() {
        loadMoreView.loadMoreStatus = BaseLoadMoreView.Status.Loading
        if (!loading) {
            loading = true
            baseQuickAdapter.weakRecyclerView.get()?.let {
                it.post { mLoadMoreListener?.invoke() }
            } ?: mLoadMoreListener?.invoke()
        }
    }

    //TODO disableLoadMoreIfNotFullPage
    /**
     * check if full page after [setNewData], if full, it will enable load more again.
     * <p>
     * 不是配置项！！
     * <p>
     * 这个方法是用来检查是否满一屏的，所以只推荐在 [setNewData] 之后使用
     * 原理很简单，先关闭 load more，检查完了再决定是否开启
     * <p>
     * 不是配置项！！
     *
     * @see setNewData
     */
    fun disableLoadMoreIfNotFullPage() {
        isEnableLoadMore = false
        val recyclerView = baseQuickAdapter.weakRecyclerView.get() ?: return
        val manager = recyclerView.layoutManager ?: return
        if (manager is LinearLayoutManager) {
            recyclerView.postDelayed({
                if (isFullScreen(manager)) {
                    isEnableLoadMore = true
                }
            }, 50)
        } else if (manager is StaggeredGridLayoutManager) {
            recyclerView.postDelayed({
                val positions = IntArray(manager.spanCount)
                manager.findLastCompletelyVisibleItemPositions(positions)
                val pos = getTheBiggestNumber(positions) + 1
                if (pos != baseQuickAdapter.itemCount) {
                    isEnableLoadMore = true
                }
            }, 50)
        }
    }

    private fun isFullScreen(llm: LinearLayoutManager): Boolean {
        return (llm.findLastCompletelyVisibleItemPosition() + 1) != baseQuickAdapter.itemCount ||
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
     * Refresh end, no more data
     *
     * @param gone if true gone the load more view
     */
    @JvmOverloads
    fun loadMoreEnd(gone: Boolean = false) {
        if (!hasLoadMoreView()) {
            return
        }
        loading = false
        mNextLoadEnable = false
        loadMoreView.isLoadEndMoreGone = gone

        loadMoreView.loadMoreStatus = BaseLoadMoreView.Status.End
        if (gone) {
            baseQuickAdapter.notifyItemRemoved(getLoadMoreViewPosition())
        } else {
            baseQuickAdapter.notifyItemChanged(getLoadMoreViewPosition())
        }
    }

    /**
     * Refresh complete
     */
    fun loadMoreComplete() {
        if (!hasLoadMoreView()) {
            return
        }
        loading = false
        mNextLoadEnable = true
        loadMoreView.loadMoreStatus = BaseLoadMoreView.Status.Complete
        baseQuickAdapter.notifyItemChanged(getLoadMoreViewPosition())
    }

    /**
     * Refresh failed
     */
    fun loadMoreFail() {
        if (!hasLoadMoreView()) {
            return
        }
        loading = false
        loadMoreView.loadMoreStatus = BaseLoadMoreView.Status.Fail
        baseQuickAdapter.notifyItemChanged(getLoadMoreViewPosition())
    }

    fun setOnLoadMoreListener(listener: OnLoadMoreListener) {
        this.mLoadMoreListener = listener
        mNextLoadEnable = true
        isEnableLoadMore = true
        loading = false
    }
}