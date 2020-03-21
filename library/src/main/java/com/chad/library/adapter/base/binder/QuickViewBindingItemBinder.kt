package com.chad.library.adapter.base.binder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 使用 ViewBinding 快速构建 Binder
 * @param T item数据类型
 * @param VB : ViewBinding
 */
abstract class QuickViewBindingItemBinder<T, VB : ViewBinding> : BaseItemBinder<T, QuickViewBindingItemBinder.BinderVBHolder<VB>>() {

    /**
     * 此 Holder 不适用于其他 BaseAdapter，仅针对[BaseBinderAdapter]
     */
    class BinderVBHolder<VB : ViewBinding>(val viewBinding: VB) : BaseViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BinderVBHolder<VB> {
        return BinderVBHolder(onCreateViewBinding(LayoutInflater.from(parent.context), parent, viewType))
    }

    abstract fun onCreateViewBinding(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): VB
}