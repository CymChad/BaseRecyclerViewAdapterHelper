package com.chad.library.adapter.base.dragswipe.listener

interface DragAndSwipeDataCallback {

    /**
     * item 数据交换
     */
    fun dataSwap(fromPosition: Int, toPosition: Int)

    /**
     * 删除 Item 数据
     *
     * @param position
     */
    fun dataRemoveAt(position: Int)
}