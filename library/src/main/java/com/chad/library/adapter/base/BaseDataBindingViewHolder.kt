package com.chad.library.adapter.base

import androidx.databinding.ViewDataBinding

abstract class BaseDataBindingViewHolder<B : ViewDataBinding>(binding: B) : BaseViewHolder(binding.root) {

}