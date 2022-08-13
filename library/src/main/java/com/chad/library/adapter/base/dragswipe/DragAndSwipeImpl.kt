package com.chad.library.adapter.base.dragswipe

import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemDragListener

/**
 * 方便自定义 DragAndSwipeCallback
 */
interface DragAndSwipeImpl {

    fun setDragMoveFlags(dragMoveFlags: Int)

    fun setSwipeMoveFlags(swipeMoveFlags: Int)

    fun setItemDragListener(onItemDragListener: OnItemDragListener?)

    fun attachToRecyclerView(@Nullable recyclerView: RecyclerView)

    fun setBaseQuickAdapter(baseQuickAdapter: BaseQuickAdapter<*, *>)

    fun startDrag(position: Int)

    fun startDrag(holder: RecyclerView.ViewHolder)

}