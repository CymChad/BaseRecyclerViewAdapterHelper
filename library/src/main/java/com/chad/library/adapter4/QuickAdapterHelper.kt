package com.chad.library.adapter4

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.leading.DefaultLeadingLoadStateAdapter
import com.chad.library.adapter4.loadState.leading.LeadingLoadStateAdapter
import com.chad.library.adapter4.loadState.trailing.DefaultTrailingLoadStateAdapter
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import java.util.Collections

/**
 * 用于组合 Adapter 的帮助类，生成一个 [ConcatAdapter]。
 *
 * 结构如下:
 *
 *                            ConcatAdapter
 * ｜-----------------------------------------------------------------｜
 * ｜                                                                 ｜
 * ｜         leadingLoadStateAdapter 或者 BeforeAdapter               ｜
 * ｜                                                                 ｜
 * ｜-----------------------------------------------------------------｜
 * ｜                                                                 ｜
 * ｜                                                                 ｜
 * ｜                        contentAdapter（内容）                    ｜
 * ｜                                                                 ｜
 * ｜                                                                 ｜
 * ｜-----------------------------------------------------------------｜
 * ｜                                                                 ｜
 * ｜         trailingLoadStateAdapter 或者 AfterAdapter               ｜
 * ｜                                                                 ｜
 * ｜-----------------------------------------------------------------｜
 *
 * 使用时，请获取此 Helper 提供的 Adapter：
 * ```
 * val helper = QuickAdapterHelper.Builder(...).build()
 *
 * recyclerView.adapter = helper.adapter
 * ```
 *
 * @property contentAdapter
 * @property leadingLoadStateAdapter
 * @property trailingLoadStateAdapter
 *
 * @param config
 */
class QuickAdapterHelper private constructor(
    val contentAdapter: BaseQuickAdapter<*, *>,

    /**
     * Adapter for loading more at the head.
     * 首部"加载跟多"Adapter
     */
    val leadingLoadStateAdapter: LeadingLoadStateAdapter<*>?,

    /**
     * Adapter for loading more at the tail.
     * 尾部"加载跟多"Adapter
     */
    val trailingLoadStateAdapter: TrailingLoadStateAdapter<*>?,

    config: ConcatAdapter.Config
) {

    private val mBeforeList = ArrayList<BaseQuickAdapter<*, *>>(0)
    private val mAfterList = ArrayList<BaseQuickAdapter<*, *>>(0)

    /**
     * The adapter which is finally attached to the RecyclerView.
     * 最终设置给 RecyclerView 的 adapter
     */
    private val mAdapter = ConcatAdapter(config)

    /**
     * 获取 Adapter
     */
    val adapter: ConcatAdapter get() = mAdapter

    /**
     * Loading state of the head.
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
     * Loading state of the tail.
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

    private var firstAdapterOnViewAttachChangeListener: BaseQuickAdapter.OnViewAttachStateChangeListener? =
        null
    private var lastAdapterOnViewAttachChangeListener: BaseQuickAdapter.OnViewAttachStateChangeListener? =
        null

    init {
        leadingLoadStateAdapter?.let {
            mAdapter.addAdapter(it)

            firstAdapterOnViewAttachChangeListener =
                object : BaseQuickAdapter.OnViewAttachStateChangeListener {

                    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
                        leadingLoadStateAdapter.checkPreload(holder.bindingAdapterPosition)
                    }

                    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {

                    }
                }.apply { contentAdapter.addOnViewAttachStateChangeListener(this) }
        }

        mAdapter.addAdapter(contentAdapter)

        trailingLoadStateAdapter?.let {
            mAdapter.addAdapter(it)

            lastAdapterOnViewAttachChangeListener =
                object : BaseQuickAdapter.OnViewAttachStateChangeListener {

                    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
                        trailingLoadStateAdapter.checkPreload(
                            holder.bindingAdapter?.itemCount ?: 0,
                            holder.bindingAdapterPosition
                        )
                    }

                    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {

                    }
                }.apply { contentAdapter.addOnViewAttachStateChangeListener(this) }
        }
    }

    /**
     * Add Adapter before [contentAdapter].
     * 在 [contentAdapter] 之前添加 Adapter
     *
     * @param adapter Adapter<*>
     */
    fun addBeforeAdapter(adapter: BaseQuickAdapter<*, *>) = apply {
        addBeforeAdapter(mBeforeList.size, adapter)
    }

    /**
     * Add Adapter before [contentAdapter].
     * 在 [contentAdapter] 之前添加 Adapter
     *
     * @param index 相对于 [contentAdapter] 的位置索引
     * @param adapter
     */
    fun addBeforeAdapter(index: Int, adapter: BaseQuickAdapter<*, *>) = apply {
        if (index < 0 || index > mBeforeList.size) throw IndexOutOfBoundsException("Index must be between 0 and ${mBeforeList.size}. Given:${index}")

        if (index == 0) {
            firstAdapterOnViewAttachChangeListener?.let {
                if (mBeforeList.isEmpty()) {
                    contentAdapter.removeOnViewAttachStateChangeListener(it)
                } else {
                    mBeforeList.first().removeOnViewAttachStateChangeListener(it)
                }
                adapter.addOnViewAttachStateChangeListener(it)
            }
        }

        val realIndex = if (leadingLoadStateAdapter == null) {
            index
        } else {
            index + 1
        }

       if (mAdapter.addAdapter(realIndex, adapter)) {
           mBeforeList += adapter
       }
    }

    /**
     * Clear all Before Adapters.
     * 清空全部的  Before Adapters
     */
    fun clearBeforeAdapters() = apply {
        mBeforeList.forEach {
            mAdapter.removeAdapter(it)
            firstAdapterOnViewAttachChangeListener?.let { listener ->
                it.removeOnViewAttachStateChangeListener(listener)
            }
        }
        mBeforeList.clear()
    }

    /**
     * Add Adapter after [contentAdapter].
     * 在 [contentAdapter] 之后添加 Adapter
     *
     * @param adapter Adapter<*>
     */
    fun addAfterAdapter(adapter: BaseQuickAdapter<*, *>) = apply {

        lastAdapterOnViewAttachChangeListener?.let {
            if (mAfterList.isEmpty()) {
                contentAdapter.removeOnViewAttachStateChangeListener(it)
            } else {
                mAfterList.last().removeOnViewAttachStateChangeListener(it)
            }
            adapter.addOnViewAttachStateChangeListener(it)
        }

        val isTure = if (trailingLoadStateAdapter == null) {
            mAdapter.addAdapter(adapter)
        } else {
            mAdapter.addAdapter(mAdapter.adapters.size - 1, adapter)
        }

        if (isTure) {
            mAfterList += adapter
        }
    }

    /**
     * Add Adapter after [contentAdapter].
     * 在 [contentAdapter] 之后添加 Adapter
     *
     * @param index
     * @param adapter
     */
    fun addAfterAdapter(index: Int, adapter: BaseQuickAdapter<*, *>) = apply {
        if (index < 0 || index > mAfterList.size) throw IndexOutOfBoundsException("Index must be between 0 and ${mAfterList.size}. Given:${index}")

        if (index == mAfterList.size) {
            addAfterAdapter(adapter)
            return@apply
        }

        val realIndex = if (trailingLoadStateAdapter == null) {
            mAdapter.adapters.size - mAfterList.size + index
        } else {
            mAdapter.adapters.size - 1 - mAfterList.size + index
        }

        if(mAdapter.addAdapter(realIndex, adapter)) {
            mAfterList += adapter
        }
    }

    /**
     * Clear AfterList.
     * 清空 AfterList
     */
    fun clearAfterAdapters() = apply {
        mAfterList.forEach {
            mAdapter.removeAdapter(it)
            lastAdapterOnViewAttachChangeListener?.let { listener ->
                it.removeOnViewAttachStateChangeListener(listener)
            }
        }
        mAfterList.clear()
    }

    /**
     * Get Adapter List before [contentAdapter]
     * 获取 [contentAdapter] 之前的 AdapterList
     */
    val beforeAdapterList: List<BaseQuickAdapter<*, *>> get() = Collections.unmodifiableList(mBeforeList)

    /**
     * Get Adapter List after [contentAdapter]
     * 获取 [contentAdapter] 之后的 AdapterList
     */
    val afterAdapterList: List<BaseQuickAdapter<*, *>> get() = Collections.unmodifiableList(mAfterList)

    fun removeAdapter(adapter: BaseQuickAdapter<*, *>) = apply {
        if (adapter == contentAdapter) {
            return@apply
        }
        mAdapter.removeAdapter(adapter)
        mBeforeList -= adapter
        mAfterList -= adapter

        firstAdapterOnViewAttachChangeListener?.let {
            adapter.removeOnViewAttachStateChangeListener(it)
            if (mBeforeList.isEmpty()) {
                contentAdapter.addOnViewAttachStateChangeListener(it)
            } else {
                mBeforeList.first().addOnViewAttachStateChangeListener(it)
            }
        }

        lastAdapterOnViewAttachChangeListener?.let {
            adapter.removeOnViewAttachStateChangeListener(it)
            if (mAfterList.isEmpty()) {
                contentAdapter.addOnViewAttachStateChangeListener(it)
            } else {
                mAfterList.last().addOnViewAttachStateChangeListener(it)
            }
        }
    }

    /**
     * Builder
     * 通过 "向前加载"、"向后加载"、主要内容Adapter，构建 [QuickAdapterHelper]
     *
     * @property contentAdapter 展示内容的 Adapter
     * @constructor Create empty Builder
     */
    class Builder(private val contentAdapter: BaseQuickAdapter<*, *>) {

        private var leadingLoadStateAdapter: LeadingLoadStateAdapter<*>? = null
        private var trailingLoadStateAdapter: TrailingLoadStateAdapter<*>? = null

        private var config: ConcatAdapter.Config = ConcatAdapter.Config.DEFAULT

        private fun getTrailingLoadStateAdapter(): TrailingLoadStateAdapter<*> {
            return trailingLoadStateAdapter ?: DefaultTrailingLoadStateAdapter().apply {
                trailingLoadStateAdapter = this
            }
        }

        /**
         * 尾部"加载更多"Adapter
         * @param loadStateAdapter LoadStateAdapter<*>?
         * @return Builder
         */
        fun setTrailingLoadStateAdapter(loadStateAdapter: TrailingLoadStateAdapter<*>?) = apply {
            this.trailingLoadStateAdapter = loadStateAdapter
        }

        fun setTrailingLoadStateAdapter(
            loadMoreListener: TrailingLoadStateAdapter.OnTrailingListener?
        ) = apply {
            getTrailingLoadStateAdapter().setOnLoadMoreListener(loadMoreListener)
        }

        /**
         * 设置“加载更多”的预加载。
         *
         * Preload, the number of items from the tail.
         *
         * @param size
         */
        fun setTrailPreloadSize(size: Int) = apply {
            getTrailingLoadStateAdapter().preloadSize = size
        }


        /**
         * 设置“加载更多”。是否打开自动加载更多
         *
         * Whether to turn on autoload more. Called when it must be initialized
         *
         * @param enable
         */
        fun isTrailAutoLoadMore(enable: Boolean) = apply {
            getTrailingLoadStateAdapter().isAutoLoadMore = enable
        }


        private fun getLeadingLoadStateAdapter(): LeadingLoadStateAdapter<*> {
            return leadingLoadStateAdapter ?: DefaultLeadingLoadStateAdapter().apply {
                leadingLoadStateAdapter = this
            }
        }

        /**
         * 首部"加载更多"Adapter
         *
         * @param loadStateAdapter
         */
        fun setLeadingLoadStateAdapter(loadStateAdapter: LeadingLoadStateAdapter<*>?) = apply {
            this.leadingLoadStateAdapter = loadStateAdapter
        }

        fun setLeadingLoadStateAdapter(
            loadListener: LeadingLoadStateAdapter.OnLeadingListener?
        ) = apply {
            getLeadingLoadStateAdapter().setOnLeadingListener(loadListener)
        }

        /**
         * 设置 ConcatAdapter 的配置
         *
         * @param config
         */
        fun setConfig(config: ConcatAdapter.Config) = apply {
            this.config = config
        }

        /**
         * 构建并返回 QuickAdapterHelper
         *
         * @return
         */
        fun build(): QuickAdapterHelper {
            return QuickAdapterHelper(
                contentAdapter,
                leadingLoadStateAdapter,
                trailingLoadStateAdapter,
                config
            )
        }

        /**
         * 附加到指定的 RecyclerView 上，并返回 QuickAdapterHelper
         *
         * @param recyclerView
         * @return
         */
        fun attachTo(recyclerView: RecyclerView): QuickAdapterHelper {
            return QuickAdapterHelper(
                contentAdapter,
                leadingLoadStateAdapter,
                trailingLoadStateAdapter,
                config
            ).apply {
                recyclerView.adapter = this.adapter
            }
        }
    }
}


