package com.chad.library.adapter.base.viewholder

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open class BaseDataBindingViewHolder<B : ViewDataBinding>(view: View) : BaseViewHolder(view) {

    fun getBinding(): B? = DataBindingUtil.getBinding(itemView)


}