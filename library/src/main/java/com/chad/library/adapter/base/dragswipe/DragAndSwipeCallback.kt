package com.chad.library.adapter.base.dragswipe

import androidx.annotation.Nullable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import java.util.*

/**
 * @author yangfeng
 * @date 2022/7/27
 */
class DragAndSwipeCallback : ItemTouchHelper.Callback() {

    private var dragMoveFlags =
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    private var swipeMoveFlags = ItemTouchHelper.END
    private var mRecyclerView: RecyclerView? = null
    private var mItemTouchHelper: ItemTouchHelper? = null


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
     * 必传
     */
    lateinit var baseQuickAdapter: BaseQuickAdapter<*, *>

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
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
        val fromPosition = viewHolder.bindingAdapterPosition
        val toPosition = target.bindingAdapterPosition
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
            return true
        }
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        baseQuickAdapter.notifyDataSetChanged()
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

}