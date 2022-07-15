package com.chad.library.adapter.base.viewholder

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * 方便 DataBinding 的使用
 *
 * @param BD : ViewDataBinding
 * @property dataBinding BD?
 * @constructor
 */
open class BaseDataBindingHolder<BD : ViewDataBinding>(view: View) : RecyclerView.ViewHolder(view) {

    val dataBinding = DataBindingUtil.bind<BD>(view)
}