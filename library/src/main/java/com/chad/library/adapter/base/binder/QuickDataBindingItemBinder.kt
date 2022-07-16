package com.chad.library.adapter.base.binder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.viewholder.QuickViewHolder

/**
 * 使用 DataBinding 快速构建 Binder
 * @param T item数据类型
 * @param DB : ViewDataBinding
 */
abstract class QuickDataBindingItemBinder<T, DB : ViewDataBinding> : BaseItemBinder<T, QuickDataBindingItemBinder.BinderDataBindingHolder<DB>>() {

    /**
     * 此 Holder 不适用于其他 BaseAdapter，仅针对[BaseBinderAdapter]
     */
    class BinderDataBindingHolder<DB : ViewDataBinding>(val dataBinding: DB) : QuickViewHolder(dataBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BinderDataBindingHolder<DB> {
        return BinderDataBindingHolder(onCreateDataBinding(LayoutInflater.from(parent.context), parent, viewType))
    }

    abstract fun onCreateDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): DB
}