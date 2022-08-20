package com.chad.library.adapter.base.dragswipe

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.chad.library.adapter.base.listener.OnItemSwipeListener
import java.util.*

/**
 * @author yangfeng
 * @date 2022/7/27
 * 默认的 拖拽类，可进行自定义，适配带有头布局的
 */
open class DefaultDragAndSwipe(
    var _dragMoveFlags: Int = ItemTouchHelper.ACTION_STATE_IDLE,
    var _swipeMoveFlags: Int = ItemTouchHelper.ACTION_STATE_IDLE,
    var _isLongPressDragEnabled: Boolean = true,
    var _isItemViewSwipeEnabled: Boolean = true
) : ItemTouchHelper.Callback(), DragAndSwipeImpl {

    private var _itemTouchHelper: ItemTouchHelper? = null
    private var mOnItemDragListener: OnItemDragListener? = null
    private var mOnItemSwipeListener: OnItemSwipeListener? = null
    private var recyclerView: RecyclerView? = null
    private var _adapterImpl: DragAndSwipeAdapterImpl? = null

    /**
     * 绑定RecyclerView
     */
    override fun attachToRecyclerView(recyclerView: RecyclerView) = apply {
        if (this.recyclerView == recyclerView) return this
        this.recyclerView = recyclerView
        if (null == _itemTouchHelper) {
            _itemTouchHelper = ItemTouchHelper(this)
            _itemTouchHelper?.attachToRecyclerView(recyclerView)
        }
    }

    override fun setDragMoveFlags(dragMoveFlags: Int) = apply {
        _dragMoveFlags = dragMoveFlags
    }

    override fun setSwipeMoveFlags(swipeMoveFlags: Int) = apply {
        _swipeMoveFlags = swipeMoveFlags
    }

    /**
     * 设置拖拽的监听
     */
    override fun setItemDragListener(onItemDragListener: OnItemDragListener?) = apply {
        this.mOnItemDragListener = onItemDragListener
    }

    override fun setItemSwipeListener(onItemSwipeListener: OnItemSwipeListener?) = apply {
        this.mOnItemSwipeListener = onItemSwipeListener
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_DRAG -> {
                mOnItemDragListener?.onItemDragStart(
                    viewHolder,
                    getViewHolderPosition(viewHolder)
                )
            }
            ItemTouchHelper.ACTION_STATE_SWIPE -> {
                mOnItemSwipeListener?.onItemSwipeStart(
                    viewHolder,
                    getViewHolderPosition(viewHolder)
                )
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    /**
     * 是否可拖动或左右滑动
     * 可根据viewHolder获取对应的条目对某条，不进行拖动或滑动操作。返回值设置为 makeMovementFlags(0, 0) 即可
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        //此处判断，是否可以长按拖动
        if (isEmptyView(viewHolder)) {
            return makeMovementFlags(0, 0)
        }
        return makeMovementFlags(_dragMoveFlags, _swipeMoveFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return viewHolder.itemViewType == target.itemViewType
    }

    override fun onMoved(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        fromPos: Int,
        target: RecyclerView.ViewHolder,
        toPos: Int,
        x: Int,
        y: Int
    ) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
        val fromPosition = getViewHolderPosition(viewHolder)
        val toPosition = getViewHolderPosition(target)
        dataSwap(fromPosition, toPosition)
        mOnItemDragListener?.onItemDragMoving(viewHolder, fromPosition, target, toPosition)
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = getViewHolderPosition(viewHolder)
        if (inRange(position)) {
            _adapterImpl?.getDragAndSwipeData()?.removeAt(position)
            _adapterImpl?.getDragAndSwipeAdapter()?.notifyItemRemoved(position)
            mOnItemSwipeListener?.onItemSwiped(viewHolder, position)
        }
    }

    override fun isLongPressDragEnabled(): Boolean {
        return _isLongPressDragEnabled
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return _isItemViewSwipeEnabled
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_SWIPE -> {
                mOnItemSwipeListener?.onItemSwipeMoving(c, viewHolder, dX, dY, isCurrentlyActive)
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        val position = getViewHolderPosition(viewHolder)
        mOnItemDragListener?.onItemDragEnd(viewHolder, position)
        mOnItemSwipeListener?.clearView(viewHolder, position)
    }

    /**
     * 进行位置的切换
     */
    private fun dataSwap(fromPosition: Int, toPosition: Int) {
        if (inRange(fromPosition) && inRange(toPosition)) {
            val data = _adapterImpl?.getDragAndSwipeData()
            if (inRange(fromPosition) && inRange(toPosition)) {
                Collections.swap(data, fromPosition, toPosition)
                _adapterImpl?.getDragAndSwipeAdapter()?.notifyItemMoved(fromPosition, toPosition)
            }
        }
    }

    override fun setAdapterImpl(adapterImpl: DragAndSwipeAdapterImpl) = apply {
        this._adapterImpl = adapterImpl
    }

    override fun setLongPressDragEnabled(isLongPressDragEnabled: Boolean) = apply {
        _isLongPressDragEnabled = isLongPressDragEnabled
    }

    override fun setItemViewSwipeEnabled(isItemViewSwipeEnabled: Boolean) = apply {
        _isItemViewSwipeEnabled = isItemViewSwipeEnabled
    }

    /**
     * 拖拽
     * 长按默认可拖动，可不进行设置此方法
     * 此方法可以做特殊使用进行调用
     * 如：长按此条position对应的item，触发 position+1 对应的item
     */
    override fun startDrag(holder: RecyclerView.ViewHolder) = apply {
        _itemTouchHelper?.startDrag(holder)
    }

    /**
     * 拖拽
     * 长按默认可拖动，可不进行设置此方法
     * 此方法可以做特殊使用进行调用
     * 如：长按此条position对应的item，触发 position+1 对应的item
     */
    override fun startDrag(position: Int) = apply {
        val holder = recyclerView?.findViewHolderForAdapterPosition(position) ?: return this
        _itemTouchHelper?.startDrag(holder)
    }

    /**
     * 启动侧滑
     */
    override fun startSwipe(holder: RecyclerView.ViewHolder) = apply {
        _itemTouchHelper?.startSwipe(holder)
    }

    /**
     * 启动侧滑
     */
    override fun startSwipe(position: Int) = apply {
        val holder = recyclerView?.findViewHolderForAdapterPosition(position) ?: return this
        _itemTouchHelper?.startSwipe(holder)
    }

    /**
     * 是否是空布局
     */
    private fun isEmptyView(viewHolder: RecyclerView.ViewHolder): Boolean {
        return viewHolder.itemViewType == BaseQuickAdapter.EMPTY_VIEW
    }

    /**
     * 防止数组下标越界
     */
    private fun inRange(position: Int): Boolean {
        val size = _adapterImpl?.getDragAndSwipeData()?.size ?: RecyclerView.NO_POSITION
        return position in 0 until size
    }

    private fun getViewHolderPosition(viewHolder: RecyclerView.ViewHolder?): Int {
        return viewHolder?.bindingAdapterPosition ?: RecyclerView.NO_POSITION
    }

    class Builder {

        private var _dragMoveFlags = ItemTouchHelper.ACTION_STATE_IDLE
        private var _swipeMoveFlags = ItemTouchHelper.ACTION_STATE_IDLE

        private var _isLongPressDragEnabled: Boolean = true

        private var _isItemViewSwipeEnabled: Boolean = true


        /**
         * 设置拖拽的flag
         */
        fun setDragMoveFlags(dragMoveFlags: Int) = apply {
            this._dragMoveFlags = dragMoveFlags
        }

        /**
         * 设置滑动的flag
         */
        fun setSwipeMoveFlags(swipeMoveFlags: Int) = apply {
            this._swipeMoveFlags = swipeMoveFlags
        }

        /**
         * 是否默认开启拖拽
         * 默认开启
         */
        fun setLongPressDragEnabled(isLongPressDragEnabled: Boolean) = apply {
            this._isLongPressDragEnabled = isLongPressDragEnabled
        }

        /**
         * 是否开启侧滑
         * 默认开启
         */
        fun setItemViewSwipeEnabled(isItemViewSwipeEnabled: Boolean) = apply {
            this._isItemViewSwipeEnabled = isItemViewSwipeEnabled
        }

        fun build(): DefaultDragAndSwipe {
            return DefaultDragAndSwipe(
                _dragMoveFlags,
                _swipeMoveFlags,
                _isLongPressDragEnabled,
                _isItemViewSwipeEnabled
            )
        }
    }

}