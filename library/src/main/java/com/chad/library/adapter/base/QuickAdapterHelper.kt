package com.chad.library.adapter.base

import androidx.annotation.Nullable
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.dragswipe.DefaultDragAndSwipe
import com.chad.library.adapter.base.dragswipe.DragAndSwipeImpl
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.chad.library.adapter.base.listener.OnItemSwipeListener
import com.chad.library.adapter.base.loadState.LoadState
import com.chad.library.adapter.base.loadState.leading.DefaultLeadingLoadStateAdapter
import com.chad.library.adapter.base.loadState.leading.LeadingLoadStateAdapter
import com.chad.library.adapter.base.loadState.trailing.DefaultTrailingLoadStateAdapter
import com.chad.library.adapter.base.loadState.trailing.TrailingLoadStateAdapter

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

    config: ConcatAdapter.Config,

    private var dragAndSwipe: DragAndSwipeImpl? = null
) {

    private val mHeaderList = ArrayList<RecyclerView.Adapter<*>>(0)
    private val mFooterList = ArrayList<RecyclerView.Adapter<*>>(0)

    /**
     * The adapter which is finally attached to the RecyclerView.
     * 最终设置给 RecyclerView 的 adapter
     */
    private val mAdapter = ConcatAdapter(config)
    val adapter: RecyclerView.Adapter<*> get() = mAdapter

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
     * Add header Adapter.
     *
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
     * Clear header.
     *
     * 清空 header
     */
    fun clearHeader() = apply {
        mHeaderList.forEach {
            mAdapter.removeAdapter(it)
        }
        mHeaderList.clear()
    }

    /**
     * Add footer adapter
     *
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
     * Clear footer.
     *
     * 清空 footer
     */
    fun clearfooter() = apply {
        mFooterList.forEach {
            mAdapter.removeAdapter(it)
        }
        mFooterList.clear()
    }

    /**
     * get header list, which can not be modified
     * 获取 header list，不可对list进行设置
     */
    val headerList: List<RecyclerView.Adapter<*>> get() = mHeaderList

    /**
     * get footer list, which can not be modified
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

    /**
     * 拖拽
     * 长按默认可拖动，可不进行设置此方法
     * 此方法可以做特殊使用进行调用
     * 如：长按此条position对应的item，触发 position+1 对应的item
     */
    fun startDrag(position: Int) = apply {
        dragAndSwipe?.startDrag(position)
    }

    /**
     * 拖拽
     * 长按默认可拖动，可不进行设置此方法
     * 此方法可以做特殊使用进行调用
     * 如：长按此条position对应的item，触发 position+1 对应的item
     */
    fun startDrag(holder: RecyclerView.ViewHolder) = apply {
        dragAndSwipe?.startDrag(holder)
    }

    /**
     * 侧滑
     */
    fun startSwipe(position: Int) = apply {
        dragAndSwipe?.startSwipe(position)
    }

    /**
     * 侧滑
     */
    fun startSwipe(holder: RecyclerView.ViewHolder) = apply {
        dragAndSwipe?.startSwipe(holder)
    }

    class Builder(private val contentAdapter: BaseQuickAdapter<*, *>) {

        private var leadingLoadStateAdapter: LeadingLoadStateAdapter<*>? = null
        private var trailingLoadStateAdapter: TrailingLoadStateAdapter<*>? = null

        private var config: ConcatAdapter.Config = ConcatAdapter.Config.DEFAULT

        var dragAndSwipeImpl: DragAndSwipeImpl? = null

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
        ) = setTrailingLoadStateAdapter(
            DefaultTrailingLoadStateAdapter().apply {
                setOnLoadMoreListener(loadMoreListener)
            }
        )

        fun setLeadingLoadStateAdapter(loadStateAdapter: LeadingLoadStateAdapter<*>?) = apply {
            this.leadingLoadStateAdapter = loadStateAdapter
        }

        fun setLeadingLoadStateAdapter(
            loadListener: LeadingLoadStateAdapter.OnLeadingListener?
        ) = setLeadingLoadStateAdapter(
            DefaultLeadingLoadStateAdapter().apply {
                setOnLeadingListener(loadListener)
            }
        )

        fun setConfig(config: ConcatAdapter.Config) = apply {
            this.config = config
        }

        /**
         * 设置自定义的拖拽
         * 一定要优先于 attachToDragAndSwipe 方法设置
         */
        fun setDragAndSwipe(dragAndSwipeImpl: DragAndSwipeImpl) = apply {
            this.dragAndSwipeImpl = dragAndSwipeImpl
        }

        /**
         * 绑定 DragAndSwipe
         * recyclerView
         * dragMoveFlags 拖动的flag
         * swipeMoveFlags 侧滑的flag
         */
        fun attachToDragAndSwipe(
            @Nullable recyclerView: RecyclerView,
            dragMoveFlags: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            swipeMoveFlags: Int = ItemTouchHelper.END
        ) = apply {
            checkDragAndSwipeCallback()
            dragAndSwipeImpl?.setBaseQuickAdapter(contentAdapter)
            dragAndSwipeImpl?.attachToRecyclerView(recyclerView)
            dragAndSwipeImpl?.setDragMoveFlags(dragMoveFlags)
            dragAndSwipeImpl?.setSwipeMoveFlags(swipeMoveFlags)
        }

        /**
         * 设置的拖动的监听
         */
        fun setItemDragListener(onItemDragListener: OnItemDragListener? = null) = apply {
            checkDragAndSwipeCallback()
            dragAndSwipeImpl?.setItemDragListener(onItemDragListener)
        }

        /**
         * 设置侧滑的监听
         */
        fun setItemSwipeListener(onItemSwipeListener: OnItemSwipeListener? = null) = apply {
            checkDragAndSwipeCallback()
            dragAndSwipeImpl?.setItemSwipeListener(onItemSwipeListener)
        }

        /**
         * 设置拖拽的flag
         */
        fun setDragMoveFlags(dragMoveFlags: Int) = apply {
            checkDragAndSwipeCallback()
            dragAndSwipeImpl?.setDragMoveFlags(dragMoveFlags)
        }

        /**
         * 设置滑动的flag
         */
        fun setSwipeMoveFlags(swipeMoveFlags: Int) = apply {
            checkDragAndSwipeCallback()
            dragAndSwipeImpl?.setSwipeMoveFlags(swipeMoveFlags)
        }

        /**
         * 是否默认开启拖拽
         * 默认开启
         */
        fun setLongPressDragEnabled(isLongPressDragEnabled: Boolean) = apply {
            checkDragAndSwipeCallback()
            dragAndSwipeImpl?.setLongPressDragEnabled(isLongPressDragEnabled)
        }

        /**
         * 是否开启侧滑
         * 默认开启
         */
        fun setItemViewSwipeEnabled(isItemViewSwipeEnabled: Boolean) = apply {
            checkDragAndSwipeCallback()
            dragAndSwipeImpl?.setItemViewSwipeEnabled(isItemViewSwipeEnabled)
        }

        /**
         * 检查默认的 DragAndSwipe 是否设置，如果没有，就用默认的
         */
        fun checkDragAndSwipeCallback() {
            if (null == dragAndSwipeImpl) {
                dragAndSwipeImpl = DefaultDragAndSwipe()
            }
        }

        fun build(): QuickAdapterHelper {
            return QuickAdapterHelper(
                contentAdapter,
                leadingLoadStateAdapter,
                trailingLoadStateAdapter,
                config,
                dragAndSwipeImpl
            )
        }

    }
}


