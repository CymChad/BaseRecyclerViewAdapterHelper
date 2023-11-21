package com.chad.library.adapter4.dragswipe.listener

/**
 * 由外部实现的数据操作
 */
interface DragAndSwipeDataCallback {

    /**
     * item 数据交换
     */
    fun dataMove(fromPosition: Int, toPosition: Int)

    /**
     * 删除 Item 数据
     *
     * @param position
     */
    fun dataRemoveAt(position: Int)
}