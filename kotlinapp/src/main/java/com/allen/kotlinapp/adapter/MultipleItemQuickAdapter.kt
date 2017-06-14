package com.allen.kotlinapp.adapter

import android.content.Context
import com.allen.kotlinapp.R
import com.allen.kotlinapp.entity.MultipleItem
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 文 件 名: MultipleItemQuickAdapter
 * 创 建 人: Allen
 * 创建日期: 2017/6/14 14:05
 * 修改时间：
 * 修改备注：
 */
class MultipleItemQuickAdapter(context: Context, data: List<MultipleItem>) : BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder>(data) {

    init {
        addItemType(MultipleItem.TEXT, R.layout.item_text_view)
        addItemType(MultipleItem.IMG, R.layout.item_image_view)
        addItemType(MultipleItem.IMG_TEXT, R.layout.item_img_text_view)
    }

    override fun convert(helper: BaseViewHolder, item: MultipleItem) {
        when (helper.itemViewType) {
            MultipleItem.TEXT -> helper.setText(R.id.tv, item.content)
            MultipleItem.IMG_TEXT -> when (helper.layoutPosition % 2) {
                0 -> helper.setImageResource(R.id.iv, R.mipmap.animation_img1)
                1 -> helper.setImageResource(R.id.iv, R.mipmap.animation_img2)
            }
        }
    }

}
