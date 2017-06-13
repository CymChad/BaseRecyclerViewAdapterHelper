package com.allen.kotlinapp.entity

import com.allen.kotlinapp.adapter.ExpandableItemAdapter
import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * 文 件 名: Level1Item
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 15:10
 * 修改时间：
 * 修改备注：
 */

class Level1Item(var title: String, var subTitle: String) : AbstractExpandableItem<Person>(), MultiItemEntity {

    override fun getItemType(): Int {
        return ExpandableItemAdapter.TYPE_LEVEL_1
    }

    override fun getLevel(): Int {
        return 1
    }
}
