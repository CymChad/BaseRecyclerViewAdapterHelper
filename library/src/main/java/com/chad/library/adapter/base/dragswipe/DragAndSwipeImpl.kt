package com.chad.library.adapter.base.dragswipe

import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.chad.library.adapter.base.listener.OnItemSwipeListener

/**
 * 方便自定义 DragAndSwipeCallback
 */
interface DragAndSwipeImpl {

    /**
     * 设置拖动的flag
     */
    fun setDragMoveFlags(dragMoveFlags: Int)

    /**
     * 设置侧滑的flag
     */
    fun setSwipeMoveFlags(swipeMoveFlags: Int)

    /**
     * 设置拖动的监听
     */
    fun setItemDragListener(onItemDragListener: OnItemDragListener?)

    /**
     * 设置侧滑的监听
     */
    fun setItemSwipeListener(onItemSwipeListener: OnItemSwipeListener?)

    /**
     * 绑定recyclerView
     */
    fun attachToRecyclerView(@Nullable recyclerView: RecyclerView)

    /**
     * 设置adapter
     */
    fun setBaseQuickAdapter(baseQuickAdapter: BaseQuickAdapter<*, *>)

    /**
     * 设置是否默认有长按拖拽功能
     */
    fun setLongPressDragEnabled(isLongPressDragEnabled: Boolean)

    /**
     * 设置是否有侧滑功能
     */
    fun setItemViewSwipeEnabled(isItemViewSwipeEnabled: Boolean)

    /**
     * 进行拖拽
     * 传入position
     */
    fun startDrag(position: Int)

    /**
     * 进行拖拽
     * 传入 viewHolder
     */
    fun startDrag(holder: RecyclerView.ViewHolder)


    /**
     * 进行侧滑
     * 传入position
     */
    fun startSwipe(position: Int)

    /**
     * 进行侧滑
     * 传入 viewHolder
     */
    fun startSwipe(holder: RecyclerView.ViewHolder)

}