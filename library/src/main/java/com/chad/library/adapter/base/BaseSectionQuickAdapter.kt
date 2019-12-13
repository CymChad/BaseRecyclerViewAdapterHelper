package com.chad.library.adapter.base

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.entity.SectionEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 快速实现带头部的 Adapter，由于本质属于多布局，所以继承自[BaseMultiItemQuickAdapter]
 * @param T : SectionEntity
 * @param VH : BaseViewHolder
 * @property sectionHeadResId Int
 * @constructor
 */
abstract class BaseSectionQuickAdapter<T : SectionEntity, VH : BaseViewHolder>
@JvmOverloads constructor(@LayoutRes private val sectionHeadResId: Int,
                          data: MutableList<T>? = null)
    : BaseMultiItemQuickAdapter<T, VH>(data) {

    constructor(@LayoutRes sectionHeadResId: Int,
                @LayoutRes layoutResId: Int,
                data: MutableList<T>? = null) : this(sectionHeadResId, data) {
        setNormalLayout(layoutResId)
    }

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

    /**
     * 如果 item 不是多布局，可以使用此方法快速设置 item layout
     * 如果需要多布局 item，请使用[addItemType]
     * @param layoutResId Int
     */
    protected fun setNormalLayout(@LayoutRes layoutResId: Int) {
        addItemType(SectionEntity.NORMAL_TYPE, layoutResId)
    }

    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) || type == SectionEntity.HEADER_TYPE
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = super.onCreateDefViewHolder(parent, viewType)
        if (viewType == SectionEntity.HEADER_TYPE) {
            setFullSpan(holder)
        }
        return holder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (holder.itemViewType == SectionEntity.HEADER_TYPE) {
//            setFullSpan(holder)
            convertHeader(holder, data[position - getHeaderLayoutCount()])
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
            convertHeader(holder, data[position - getHeaderLayoutCount()], payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

}