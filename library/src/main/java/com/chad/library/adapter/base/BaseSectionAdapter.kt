package com.chad.library.adapter.base

import android.view.ViewGroup
import androidx.annotation.IntRange
import com.chad.library.adapter.base.entity.NSectionEntity
import com.chad.library.adapter.base.entity.SectionExpandableEntity
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.provider.BaseSectionItemProvider

abstract class BaseSectionAdapter<VH : BaseViewHolder>(data: MutableList<NSectionEntity>? = null)
    : BaseProviderMultiAdapter<NSectionEntity, VH>(data) {

    private val sectionHeaderSet = HashSet<Int>()
    private val sectionFooterSet = HashSet<Int>()

    init {
        if (data != null) {
            val flatData = flatData(data)
            data.clear()
            data.addAll(flatData)
        }
    }


    /**
     * 必须通过此方法，添加 provider
     * @param provider BaseItemProvider
     */
    fun addSectionItemProvider(provider: BaseSectionItemProvider<VH>) {
        addItemProvider(provider)
    }

    fun addSectionHeaderProvider(provider: BaseSectionItemProvider<VH>) {
        sectionHeaderSet.add(provider.itemViewType)
        addItemProvider(provider)
    }

    fun addSectionFooterProvider(provider: BaseSectionItemProvider<VH>) {
        sectionFooterSet.add(provider.itemViewType)
        addItemProvider(provider)
    }

    override fun addItemProvider(provider: BaseItemProvider<NSectionEntity, VH>) {
        if (provider is BaseSectionItemProvider) {
            super.addItemProvider(provider)
        } else {
            throw IllegalStateException("please add BaseSectionItemProvider, no BaseItemProvider!")
        }
    }

    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) || sectionHeaderSet.contains(type) || sectionFooterSet.contains(type)
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = super.onCreateDefViewHolder(parent, viewType)
        if (sectionHeaderSet.contains(viewType) || sectionFooterSet.contains(viewType)) {
            setFullSpan(holder)
        }
        return holder
    }

//    override fun getDefItemCount(): Int {
//        allDataCount(data, 0)
//        return super.getDefItemCount()
//    }

    override fun setListNewData(data: MutableList<NSectionEntity>?) {
        this.data = if (data == null) {
            arrayListOf()
        } else {
            val f = flatData(data)
            println("f-----------" + f.size)
            f
        }
    }

    private fun flatData(list: List<NSectionEntity>): MutableList<NSectionEntity> {
        val newList = ArrayList<NSectionEntity>()
        for (element in list) {
            if (element.headerEntity != null) {
                newList.add(element.headerEntity!!)
            }

            if (element is SectionExpandableEntity && !element.isExpanded) {
                // 不处理
            } else {
                val subItem = element.subItem
                if (!subItem.isNullOrEmpty()) {
                    val items = flatSubItemData(subItem)
                    newList.addAll(items)
                }
            }

//            if (element.isHaveSubItem) {
//                val items: MutableList<NSectionEntity> = if (subItem.isNullOrEmpty()) {
//                    arrayListOf()
//                } else {
//                    flatData(subItem)
//                }
//                newList.addAll(items)
//            } else {
//                newList.add(element)
//            }

            if (element.footerEntity != null) {
                newList.add(element.footerEntity!!)
            }
        }
        return newList
//        return list.flatMapTo(newList, {
//            newList.add(it)
//
//            val subItem = it.subItem
//            if (subItem == null) {
//                ArrayList()
//            } else {
//                flatData(subItem)
//            }
//
//        })
    }

    private fun flatSubItemData(list: List<NSectionEntity>): MutableList<NSectionEntity> {
        val newList = ArrayList<NSectionEntity>()
        for (element in list) {
            element.headerEntity?.let {
                newList.add(it)
            }

            if (element is SectionExpandableEntity && !element.isExpanded) {
                newList.add(element)
            } else {
                val subItem = element.subItem
                if (subItem.isNullOrEmpty()) {
                    // 表示没有子项了
                    newList.add(element)
                } else {
                    val items = flatSubItemData(subItem)
                    newList.addAll(items)
                }
            }
//            val subItem = element.subItem
//            if (subItem.isNullOrEmpty()) {
//                newList.add(element)
//            } else {
//                val items = flatSubItemData(subItem)
//                newList.addAll(items)
//            }

            if (element.footerEntity != null) {
                newList.add(element.footerEntity!!)
            }
        }
        return newList
    }

    /**
     * 收起展开项目
     * @param position Int
     * @param animate Boolean
     * @param notify Boolean
     */
    fun collapse(@IntRange(from = 0) position: Int, animate: Boolean = true, notify: Boolean = true) {
        val section = this.data[position]

        println("------------>>>>1  ${section is SectionExpandableEntity}")
        println("------------>>>>2  ${section is SectionExpandableEntity && section.isExpanded}")

        if (section is SectionExpandableEntity && section.isExpanded) {
            if (section.subItem.isNullOrEmpty()) {
                section.isExpanded = false
                notifyItemChanged(position)
                return
            }
//            if (section.subItem.isNullOrEmpty()) {
//
//            }
            val items = flatSubItemData(section.subItem!!)
            this.data.removeAll(items)
            notifyItemChanged(position)
            notifyItemRangeRemoved(position + 1, items.size)
        }
    }

    //统计
    private fun allDataCount(list: List<NSectionEntity>, count: Int): Int {
        var newCount = list.size + count
        list.map {
            if (!it.subItem.isNullOrEmpty()) {
                newCount += allDataCount(it.subItem!!, newCount)
            }
        }
        return newCount
    }
}