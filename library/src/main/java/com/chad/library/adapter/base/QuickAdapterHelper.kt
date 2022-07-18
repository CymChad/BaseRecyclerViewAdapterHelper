package com.chad.library.adapter.base

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.loadState.LoadState
import com.chad.library.adapter.base.loadState.LoadStateAdapter
import com.chad.library.adapter.base.loadState.leading.DefaultLeadingLoadStateAdapter
import com.chad.library.adapter.base.loadState.leading.LeadingLoadStateAdapter
import com.chad.library.adapter.base.loadState.trailing.DefaultTrailingLoadStateAdapter
import com.chad.library.adapter.base.loadState.trailing.TrailingLoadStateAdapter

class QuickAdapterHelper private constructor(
    val contentAdapter: BaseQuickAdapter<*, *>,

    /**
     * 首部"加载跟多"Adapter
     */
    val leadingLoadStateAdapter: LeadingLoadStateAdapter<*>?,

    /**
     * 尾部"加载跟多"Adapter
     */
    val trailingLoadStateAdapter: TrailingLoadStateAdapter<*>?,

    config: ConcatAdapter.Config
) {

    private val mHeaderList = ArrayList<RecyclerView.Adapter<*>>(0)
    private val mFooterList = ArrayList<RecyclerView.Adapter<*>>(0)

    /**
     * 最终设置给 RecyclerView 的 adapter
     */
    private val mAdapter = ConcatAdapter(config)
    val adapter: RecyclerView.Adapter<*> get() = mAdapter

    /**
     * 首部的加载状态
     */
    var leadingLoadState: LoadState
        set(value) {
            leadingLoadStateAdapter?.loadState = value
        }
        get() {
            return leadingLoadStateAdapter?.loadState
                ?: LoadState.NotLoading(endOfPaginationReached = false)
        }

    /**
     * 尾部的加载状态
     */
    var trailingLoadState: LoadState
        set(value) {
            trailingLoadStateAdapter?.loadState = value
        }
        get() {
            return trailingLoadStateAdapter?.loadState
                ?: LoadState.NotLoading(endOfPaginationReached = false)
        }

    init {
        leadingLoadStateAdapter?.let {
            mAdapter.addAdapter(it)

            contentAdapter.addOnViewAttachStateChangeListener(object :
                BaseQuickAdapter.OnViewAttachStateChangeListener {

                override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
                    leadingLoadStateAdapter.checkPreload(holder.bindingAdapterPosition)
                }

                override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {

                }
            })
        }

        mAdapter.addAdapter(contentAdapter)

        trailingLoadStateAdapter?.let {
            mAdapter.addAdapter(it)

            contentAdapter.addOnViewAttachStateChangeListener(object :
                BaseQuickAdapter.OnViewAttachStateChangeListener {

                override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
                    trailingLoadStateAdapter.checkPreload(
                        contentAdapter.items.size,
                        holder.bindingAdapterPosition
                    )
                }

                override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {

                }
            })
        }
    }

    /**
     * 添加首部 header Adapter
     * @param headerAdapter Adapter<*>
     * @return QuickAdapterHelper
     */
    fun addHeader(headerAdapter: RecyclerView.Adapter<*>) = apply {
        addHeader(mHeaderList.size, headerAdapter)
    }

    fun addHeader(index: Int, headerAdapter: RecyclerView.Adapter<*>) = apply {
        if (index < 0 || index > mHeaderList.size) throw IndexOutOfBoundsException("Index must be between 0 and ${mHeaderList.size}. Given:${index}")

        val startIndex = if (leadingLoadStateAdapter == null) {
            0
        } else {
            1
        }

        mAdapter.addAdapter(startIndex + index, headerAdapter)
        mHeaderList.add(headerAdapter)
    }

    /**
     * 清空 header
     */
    fun clearHeader() = apply {
        mHeaderList.forEach {
            mAdapter.removeAdapter(it)
        }
        mHeaderList.clear()
    }

    /**
     * 添加脚部 footer adapter
     * @param footerAdapter Adapter<*>
     * @return QuickAdapterHelper
     */
    fun addFooter(footerAdapter: RecyclerView.Adapter<*>) = apply {
        if (trailingLoadStateAdapter == null) {
            mAdapter.addAdapter(footerAdapter)
        } else {
            mAdapter.addAdapter(mAdapter.adapters.size - 1, footerAdapter)
        }
        mFooterList.add(footerAdapter)
    }

    fun addFooter(index: Int, footerAdapter: RecyclerView.Adapter<*>) = apply {
        if (index < 0 || index > mFooterList.size) throw IndexOutOfBoundsException("Index must be between 0 and ${mFooterList.size}. Given:${index}")

        val realIndex = if (trailingLoadStateAdapter == null) {
            mAdapter.adapters.size - mFooterList.size + index
        } else {
            mAdapter.adapters.size - 1 - mFooterList.size + index
        }

        mAdapter.addAdapter(realIndex, footerAdapter)
        mFooterList.add(footerAdapter)
    }

    /**
     * 清空 footer
     */
    fun clearfooter() = apply {
        mFooterList.forEach {
            mAdapter.removeAdapter(it)
        }
        mFooterList.clear()
    }

    /**
     * 获取 header list，不可对list进行设置
     */
    val headerList: List<RecyclerView.Adapter<*>> get() = mHeaderList

    /**
     *  获取 footer list，不可对list进行设置
     */
    val footerList: List<RecyclerView.Adapter<*>> get() = mFooterList

    fun removeAdapter(a: RecyclerView.Adapter<*>) = apply {
        if (a == contentAdapter) {
            return@apply
        }

        mAdapter.removeAdapter(a)
        mHeaderList.remove(a)
        mFooterList.remove(a)
    }

    class Builder(private val contentAdapter: BaseQuickAdapter<*, *>) {

        private var leadingLoadStateAdapter: LeadingLoadStateAdapter<*>? = null
        private var trailingLoadStateAdapter: TrailingLoadStateAdapter<*>? = null

        private var config: ConcatAdapter.Config = ConcatAdapter.Config.DEFAULT

        /**
         * 尾部"加载更多"Adapter
         * @param loadStateAdapter LoadStateAdapter<*>?
         * @return Builder
         */
        fun setTrailingLoadStateAdapter(loadStateAdapter: TrailingLoadStateAdapter<*>?) = apply {
            this.trailingLoadStateAdapter = loadStateAdapter
        }

        fun setTrailingLoadStateAdapter(
            loadMoreListener: TrailingLoadStateAdapter.OnTrailingListener?,
            loadStateListener: LoadStateAdapter.OnAllowLoadingListener?
        ) = setTrailingLoadStateAdapter(
            DefaultTrailingLoadStateAdapter().apply {
                setOnLoadMoreListener(loadMoreListener)
                setOnAllowLoadingListener(loadStateListener)
            }
        )

        fun setLeadingLoadStateAdapter(loadStateAdapter: LeadingLoadStateAdapter<*>?) = apply {
            this.leadingLoadStateAdapter = loadStateAdapter
        }

        fun setLeadingLoadStateAdapter(
            loadListener: LeadingLoadStateAdapter.OnLeadingListener?,
            loadStateListener: LoadStateAdapter.OnAllowLoadingListener?
        ) = setLeadingLoadStateAdapter(
            DefaultLeadingLoadStateAdapter().apply {
                setOnLeadingListener(loadListener)
                setOnAllowLoadingListener(loadStateListener)
            }
        )

        fun setConfig(config: ConcatAdapter.Config) = apply {
            this.config = config
        }

        fun build(): QuickAdapterHelper {
            return QuickAdapterHelper(
                contentAdapter,
                leadingLoadStateAdapter,
                trailingLoadStateAdapter,
                config
            )
        }

    }
}


