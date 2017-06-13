package com.allen.kotlinapp.entity

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * 文 件 名: ClickEntity
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 15:08
 * 修改时间：
 * 修改备注：
 */
class ClickEntity(var Type: Int) : MultiItemEntity {

    override fun getItemType(): Int {
        return Type
    }

    companion object {
        val CLICK_ITEM_VIEW = 1
        val CLICK_ITEM_CHILD_VIEW = 2
        val LONG_CLICK_ITEM_VIEW = 3
        val LONG_CLICK_ITEM_CHILD_VIEW = 4
        val NEST_CLICK_ITEM_CHILD_VIEW = 5
    }
}
