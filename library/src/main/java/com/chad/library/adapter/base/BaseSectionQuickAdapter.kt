package com.chad.library.adapter.base

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.entity.SectionEntity

/**
 * 带头部的 Adapter，由于本质属于多布局，所以继承自[BaseMultiItemQuickAdapter]
 * @param T : SectionEntity
 * @param VH : BaseViewHolder
 * @property sectionHeadResId Int
 * @constructor
 */
abstract class BaseSectionQuickAdapter<T : SectionEntity, VH : BaseViewHolder>
@JvmOverloads constructor(@LayoutRes private val sectionHeadResId: Int,
                          data: MutableList<T>? = null)
    : BaseMultiItemQuickAdapter<T, VH>(data) {


    init {
        addItemType(SectionEntity.HEADER_TYPE, sectionHeadResId)
    }

    /**
     * 重写此处，设置 Header
     * @param helper ViewHolder
     * @param item data
     */
    protected abstract fun convertHeader(helper: VH, item: T?)

    /**
     * 重写此处，设置 Diff Header
     * @param helper VH
     * @param item T?
     * @param payloads MutableList<Any>
     */
    protected fun convertHeader(helper: VH, item: T?, payloads: MutableList<Any>) {}

    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) || type == SectionEntity.HEADER_TYPE
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (holder.itemViewType == SectionEntity.HEADER_TYPE) {
            setFullSpan(holder)
            convertHeader(holder, getRealItem(position))
        } else {
            super.onBindViewHolder(holder, position)
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }

        if (holder.itemViewType == SectionEntity.HEADER_TYPE) {
            convertHeader(holder, getRealItem(position), payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

}