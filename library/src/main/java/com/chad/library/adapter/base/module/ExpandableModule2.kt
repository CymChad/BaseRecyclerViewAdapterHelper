package com.chad.library.adapter.base.module

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.NSectionEntity

/**
 * 需要【向上加载更多】功能的，[BaseQuickAdapter]继承此接口
 */
interface ExpandableModule2

open class BaseExpandableModule2(private val adapter: BaseNodeAdapter<*>) {

    var isDefaultExpandAll = true

    fun expandAll() {
        adapter.data
    }

    private fun flatData(list: List<NSectionEntity>): MutableList<NSectionEntity> {
        val newList = ArrayList<NSectionEntity>()
        for (element in list) {
            if (element.headerEntity != null) {
                newList.add(element.headerEntity!!)
            }

            val subItem = element.subItem
            if (!subItem.isNullOrEmpty()) {
                val items = flatSubItemData(subItem)
                newList.addAll(items)
            }
            if (element.footerEntity != null) {
                newList.add(element.footerEntity!!)
            }
        }
        return newList
    }

    private fun flatSubItemData(list: List<NSectionEntity>): MutableList<NSectionEntity> {
        val newList = ArrayList<NSectionEntity>()
        for (element in list) {
            if (element.headerEntity != null) {
                newList.add(element.headerEntity!!)
            }

            val subItem = element.subItem
            if (subItem.isNullOrEmpty()) {
                newList.add(element)
            } else {
                val items = flatSubItemData(subItem)
                newList.addAll(items)
            }

            if (element.footerEntity != null) {
                newList.add(element.footerEntity!!)
            }
        }
        return newList
    }
}