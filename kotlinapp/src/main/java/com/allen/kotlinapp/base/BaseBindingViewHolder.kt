package com.allen.kotlinapp.base

import android.databinding.ViewDataBinding
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 文 件 名: BaseBindingViewHolder
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 15:05
 * 修改时间：
 * 修改备注：
 */
class BaseBindingViewHolder<Binding : ViewDataBinding>(view: View) : BaseViewHolder(view) {
    var binding: Binding? = null
}
