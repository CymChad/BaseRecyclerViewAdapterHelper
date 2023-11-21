package com.chad.baserecyclerviewadapterhelper.activity.dragswipe

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chad.baserecyclerviewadapterhelper.activity.dragswipe.adapter.HeaderDragAndSwipeAdapter
import com.chad.library.adapter4.dragswipe.QuickDragAndSwipe

/**
 * 重写拖拽类，根据itemType 设置某个类型的是否允许拖拽
 */
class HeaderDragAndSwipe : QuickDragAndSwipe() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (recyclerView.adapter is ConcatAdapter) {
            val adapter = recyclerView.adapter as ConcatAdapter
            val absoluteAdapter =
                adapter.getWrappedAdapterAndPosition(viewHolder.absoluteAdapterPosition).first
            if (absoluteAdapter is HeaderDragAndSwipeAdapter) {
                return super.getMovementFlags(recyclerView, viewHolder)
            }
            return makeMovementFlags(0, 0)
        }
        return makeMovementFlags(0, 0)
    }
}