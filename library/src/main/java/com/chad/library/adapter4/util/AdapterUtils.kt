package com.chad.library.adapter4.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * An extension function to getItemView.
 * 扩展方法，用于获取View
 * @receiver ViewGroup parent
 * @param layoutResId Int
 * @return View
 */
fun ViewGroup.getItemView(@LayoutRes layoutResId: Int): View {
    return LayoutInflater.from(this.context).inflate(layoutResId, this, false)
}

/**
 * If the hold view use StaggeredGridLayoutManager they should using all span area.
 * 如果 ViewHolder 使用 StaggeredGridLayoutManager 布局，则铺满一行。
 */
fun RecyclerView.ViewHolder.asStaggeredGridFullSpan() {
    if (this.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
        (this.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
    }
}