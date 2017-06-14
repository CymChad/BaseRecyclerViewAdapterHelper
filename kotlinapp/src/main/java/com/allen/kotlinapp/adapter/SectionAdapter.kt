package com.allen.kotlinapp.adapter

import com.allen.kotlinapp.R
import com.allen.kotlinapp.entity.MySection
import com.allen.kotlinapp.entity.Video
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 文 件 名: SectionAdapter
 * 创 建 人: Allen
 * 创建日期: 2017/6/14 18:53
 * 修改时间：
 * 修改备注：
 */
/**
 * Same as QuickAdapter#QuickAdapter(Context,int) but with
 * some initialization data.

 * @param sectionHeadResId The section head layout id for each item
 * *
 * @param layoutResId      The layout resource id of each item.
 * *
 * @param data             A new list is created out of this one to avoid mutable list
 */
class SectionAdapter constructor(layoutResId: Int, sectionHeadResId: Int, data: List<MySection>) : BaseSectionQuickAdapter<MySection, BaseViewHolder>(layoutResId, sectionHeadResId, data) {

    override fun convertHead(helper: BaseViewHolder, item: MySection) {
        helper.setText(R.id.header, item.header)
        helper.setVisible(R.id.more, item.isMore)
        helper.addOnClickListener(R.id.more)
    }


    override fun convert(helper: BaseViewHolder, item: MySection) {
        val video = item.t as Video
        when (helper.layoutPosition % 2) {
            0 -> helper.setImageResource(R.id.iv, R.mipmap.m_img1)
            1 -> helper.setImageResource(R.id.iv, R.mipmap.m_img2)
        }
        helper.setText(R.id.tv, video.name)
    }
}
