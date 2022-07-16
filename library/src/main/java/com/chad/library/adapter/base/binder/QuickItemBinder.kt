package com.chad.library.adapter.base.binder

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.util.getItemView
import com.chad.library.adapter.base.viewholder.QuickViewHolder

/**
 * 使用布局 ID 快速构建 Binder
 * @param T item 数据类型
 */
abstract class QuickItemBinder<T> : BaseItemBinder<T, QuickViewHolder>() {

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickViewHolder =
            QuickViewHolder(parent.getItemView(getLayoutId()))

}

