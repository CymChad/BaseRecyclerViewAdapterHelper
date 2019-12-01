package com.chad.library.adapter.base.provider

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.lang.ref.WeakReference

abstract class BaseItemProvider<T, VH : BaseViewHolder> {

    lateinit var context: Context
    lateinit var weakAdapter: WeakReference<BaseQuickAdapter<T, VH>>

    abstract val itemViewType: Int

    abstract val layoutId: Int
        @LayoutRes
        get

    abstract fun convert(@NonNull helper: VH, data: T?)

    fun convert(@NonNull helper: VH, data: T?, payloads: List<Any>) {}

    /**
     * item 若想实现条目点击事件则重写该方法
     * @param helper VH
     * @param data T
     * @param position Int
     */
    fun onClick(helper: VH, data: T, position: Int) {}

    /**
     * item 若想实现条目长按事件则重写该方法
     * @param helper VH
     * @param data T
     * @param position Int
     * @return Boolean
     */
    fun onLongClick(helper: VH, data: T, position: Int): Boolean {
        return false
    }
}