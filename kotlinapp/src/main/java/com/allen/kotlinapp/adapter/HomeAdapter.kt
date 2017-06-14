package com.allen.kotlinapp.adapter

import com.allen.kotlinapp.R
import com.allen.kotlinapp.entity.HomeItem
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 文 件 名: HomeAdapter
 * 创 建 人: Allen
 * 创建日期: 2017/6/14 13:59
 * 修改时间：
 * 修改备注：
 */
class HomeAdapter(layoutResId: Int, data: List<HomeItem>) : BaseQuickAdapter<HomeItem, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: HomeItem) {
        helper.setText(R.id.text, item.title)
        helper.setImageResource(R.id.icon, item.imageResource)
    }
}
