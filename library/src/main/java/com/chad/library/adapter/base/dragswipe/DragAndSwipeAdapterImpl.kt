package com.chad.library.adapter.base.dragswipe

import androidx.recyclerview.widget.RecyclerView

interface DragAndSwipeAdapterImpl {
    /**
     * 获取adapter的实例
     */
    fun getDragAndSwipeAdapter(): RecyclerView.Adapter<*>

    /**
     * 获取adapter的数据源，可进行数据的删除与交换操作
     */
    fun getDragAndSwipeData(): List<*>
}