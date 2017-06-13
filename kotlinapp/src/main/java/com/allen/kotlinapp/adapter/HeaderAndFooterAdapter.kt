package com.allen.kotlinapp.adapter

import com.allen.kotlinapp.R
import com.allen.kotlinapp.data.DataServer
import com.allen.kotlinapp.entity.Status
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 文 件 名: HeaderAndFooterAdapter
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 15:40
 * 修改时间：
 * 修改备注：
 */
class HeaderAndFooterAdapter(dataSize: Int) : BaseQuickAdapter<Status, BaseViewHolder>(R.layout.item_header_and_footer, DataServer.getSampleData(dataSize)) {

    override fun convert(helper: BaseViewHolder, item: Status) {
        when (helper.layoutPosition % 3) {
            0 -> helper.setImageResource(R.id.iv, R.mipmap.animation_img1)
            1 -> helper.setImageResource(R.id.iv, R.mipmap.animation_img2)
            2 -> helper.setImageResource(R.id.iv, R.mipmap.animation_img3)
        }
    }


}
