package com.chad.library.adapter.base.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

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