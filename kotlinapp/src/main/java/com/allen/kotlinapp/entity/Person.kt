package com.allen.kotlinapp.entity

import com.allen.kotlinapp.adapter.ExpandableItemAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * 文 件 名: Person
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 15:11
 * 修改时间：
 * 修改备注：
 */
class Person(var name: String, var age: Int) : MultiItemEntity {

    override fun getItemType(): Int {
        return ExpandableItemAdapter.TYPE_PERSON
    }
}
