package com.chad.library.adapter.base.viewholder

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View


/**
 * 方便 DataBinding 的使用
 *
 * @param BD : ViewDataBinding
 * @property dataBinding BD?
 * @constructor
 */
open class BaseDataBindingHolder<BD : ViewDataBinding>(view: View) : BaseViewHolder(view) {

    val dataBinding = DataBindingUtil.bind<BD>(view)
}