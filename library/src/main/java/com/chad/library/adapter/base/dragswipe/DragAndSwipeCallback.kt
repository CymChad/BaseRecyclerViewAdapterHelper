package com.chad.library.adapter.base.dragswipe

import androidx.annotation.Nullable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemDragListener
import java.util.*

/**
 * @author yangfeng
 * @date 2022/7/27
 */
open class DragAndSwipeCallback : ItemTouchHelper.Callback() {

    var dragMoveFlags =
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    var swipeMoveFlags = ItemTouchHelper.END
    private var mRecyclerView: RecyclerView? = null
    private var mItemTouchHelper: ItemTouchHelper? = null
    private var mOnItemDragListener: OnItemDragListener? = null

    /**
     * 绑定RecyclerView
     */
    fun attachToRecyclerView(@Nullable recyclerView: RecyclerView) {
        if (this.mRecyclerView == recyclerView) return
        mRecyclerView = recyclerView
        if (null == mItemTouchHelper) {
            mItemTouchHelper = ItemTouchHelper(this)
            mItemTouchHelper?.attachToRecyclerView(recyclerView)
        }
    }

    /**
     * 设置拖拽的监听
     */
    fun setItemDragListener(onItemDragListener: OnItemDragListener? = null) {
        this.mOnItemDragListener = onItemDragListener
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_DRAG -> {
                mOnItemDragListener?.onItemDragStart(
                    viewHolder,
                    getViewHolderPosition(viewHolder)
                )
            }
        }
    }

    /**
     * 必传
     */
    lateinit var baseQuickAdapter: BaseQuickAdapter<*, *>

    /**
     * 是否可拖动或左右滑动
     * 当含有列表头或者尾时，进行重写 makeMovementFlags(0, 0)
     * 不进行拖动或滑动操作
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        //此处判断，是否可以长按拖动
        if (isEmptyView(viewHolder)) {
            return makeMovementFlags(0, 0)
        }
        return makeMovementFlags(dragMoveFlags, swipeMoveFlags)
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
        dataSwap(fromPos, toPos)
        mOnItemDragListener?.onItemDragMoving(viewHolder, fromPos, target, toPos)
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        mOnItemDragListener?.onItemDragEnd(viewHolder, getViewHolderPosition(viewHolder))
    }

    /**
     * 进行位置的切换
     */
    private fun dataSwap(fromPosition: Int, toPosition: Int) {
        if (inRange(fromPosition) && inRange(toPosition)) {
            val data = baseQuickAdapter.items
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(data, i, i + 1)
                }
            } else {
                val toP = toPosition + 1
                for (i in fromPosition downTo toP) {
                    Collections.swap(data, i, i - 1)
                }
            }
            baseQuickAdapter.notifyItemMoved(fromPosition, toPosition)
        }
    }

    fun startDrag(holder: RecyclerView.ViewHolder) {
        mItemTouchHelper?.startDrag(holder)
    }

    /**
     * 启动拖拽
     */
    fun startDrag(position: Int) {
        val holder = mRecyclerView?.findViewHolderForAdapterPosition(position) ?: return
        mItemTouchHelper?.startDrag(holder)
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
        val size = baseQuickAdapter.items.size
        return position in 0 until size
    }

    private fun getViewHolderPosition(viewHolder: RecyclerView.ViewHolder?): Int {
        return viewHolder?.bindingAdapterPosition ?: RecyclerView.NO_POSITION
    }

}