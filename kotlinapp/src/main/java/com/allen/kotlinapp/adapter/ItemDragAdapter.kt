package com.allen.kotlinapp.adapter

import com.allen.kotlinapp.R
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 文 件 名: ItemDragAdapter
 * 创 建 人: Allen
 * 创建日期: 2017/6/14 14:04
 * 修改时间：
 * 修改备注：
 */
class ItemDragAdapter(data: List<String>) : BaseItemDraggableAdapter<String, BaseViewHolder>(R.layout.item_draggable_view, data) {

    override fun convert(helper: BaseViewHolder, item: String) {
        when (helper.layoutPosition % 3) {
            0 -> helper.setImageResource(R.id.iv_head, R.mipmap.head_img0)
            1 -> helper.setImageResource(R.id.iv_head, R.mipmap.head_img1)
            2 -> helper.setImageResource(R.id.iv_head, R.mipmap.head_img2)
        }
        helper.setText(R.id.tv, item)
    }
}
