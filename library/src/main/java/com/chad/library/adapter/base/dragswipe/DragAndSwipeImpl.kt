package com.chad.library.adapter.base.dragswipe

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.chad.library.adapter.base.listener.OnItemSwipeListener

/**
 * 方便自定义 DragAndSwipeCallback
 */
interface DragAndSwipeImpl {

    /**
     * 绑定recyclerView
     */
    fun attachToRecyclerView(recyclerView: RecyclerView): DefaultDragAndSwipe

    /**
     * 设置拖动的flag
     */
    fun setDragMoveFlags(dragMoveFlags: Int): DefaultDragAndSwipe

    /**
     * 设置侧滑的flag
     */
    fun setSwipeMoveFlags(swipeMoveFlags: Int): DefaultDragAndSwipe

    /**
     * 设置拖动的监听
     */
    fun setItemDragListener(onItemDragListener: OnItemDragListener?): DefaultDragAndSwipe

    /**
     * 设置侧滑的监听
     */
    fun setItemSwipeListener(onItemSwipeListener: OnItemSwipeListener?): DefaultDragAndSwipe

    /**
     * 设置adapter
     */
    fun setAdapterImpl(adapterImpl: DragAndSwipeAdapterImpl): DefaultDragAndSwipe

    /**
     * 设置是否默认有长按拖拽功能
     */
    fun setLongPressDragEnabled(isLongPressDragEnabled: Boolean): DefaultDragAndSwipe

    /**
     * 设置是否有侧滑功能
     */
    fun setItemViewSwipeEnabled(isItemViewSwipeEnabled: Boolean): DefaultDragAndSwipe

    /**
     * 进行拖拽
     * 传入position
     */
    fun startDrag(position: Int): DefaultDragAndSwipe

    /**
     * 进行拖拽
     * 传入 viewHolder
     */
    fun startDrag(holder: RecyclerView.ViewHolder): DefaultDragAndSwipe


    /**
     * 进行侧滑
     * 传入position
     */
    fun startSwipe(position: Int): DefaultDragAndSwipe

    /**
     * 进行侧滑
     * 传入 viewHolder
     */
    fun startSwipe(holder: RecyclerView.ViewHolder): DefaultDragAndSwipe

}