package com.chad.library.adapter.base.util

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.R
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * @author 李沐阳
 * @date 2023/5/29
 * @description
 */
private abstract class DebouncedClickListener<T>(private val interval: Long) :
    BaseQuickAdapter.OnItemClickListener<T>, BaseQuickAdapter.OnItemChildClickListener<T> {
    private var mLastClickTime: Long = 0

    override fun onClick(adapter: BaseQuickAdapter<T, *>, view: View, position: Int) {
        val nowTime = System.currentTimeMillis()
        val diffTime = nowTime - mLastClickTime
        // 当用户修改系统时间时，可能会导致diffTime为负数
        if (diffTime >= interval || diffTime < 0) {
            mLastClickTime = nowTime
            onSingleClick(adapter, view, position)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<T, *>, view: View, position: Int) {
        val nowTime = System.currentTimeMillis()
        val diffTime = nowTime - mLastClickTime
        // 当用户修改系统时间时，可能会导致diffTime为负数
        if (diffTime >= interval || diffTime < 0) {
            mLastClickTime = nowTime
            onSingleClick(adapter, view, position)
        }
    }

    protected abstract fun onSingleClick(adapter: BaseQuickAdapter<T, *>, view: View, position: Int)
}


/**
 * 去除点击抖动的点击方法
 *
 * @param time 间隔时间，单位：毫秒
 * @param block
 * @receiver
 */
fun <T, VH : RecyclerView.ViewHolder> BaseQuickAdapter<T, VH>.setOnDebouncedItemClick(
    time: Long = 500,
    block: BaseQuickAdapter.OnItemClickListener<T>
) = this.setOnItemClickListener(object : DebouncedClickListener<T>(time) {
    override fun onSingleClick(adapter: BaseQuickAdapter<T, *>, view: View, position: Int) {
        block.onClick(adapter, view, position)
    }
})

/**
 * 去除点 Child View 击抖动的点击方法
 *
 * @param time 间隔时间，单位：毫秒
 * @param block
 * @receiver
 */
fun <T, VH : RecyclerView.ViewHolder> BaseQuickAdapter<T, VH>.addOnDebouncedChildClick(
    @IdRes id: Int,
    time: Long = 500,
    block: BaseQuickAdapter.OnItemChildClickListener<T>
) = this.addOnItemChildClickListener(id, object : DebouncedClickListener<T>(time) {
    override fun onSingleClick(adapter: BaseQuickAdapter<T, *>, view: View, position: Int) {
        block.onItemClick(adapter, view, position)
    }
})
