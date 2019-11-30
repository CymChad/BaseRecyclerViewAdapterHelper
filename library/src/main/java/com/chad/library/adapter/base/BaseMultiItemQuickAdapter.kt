package com.chad.library.adapter.base

import android.util.SparseIntArray
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.entity.MultiItemEntity

abstract class BaseMultiItemQuickAdapter<T : MultiItemEntity, VH : BaseViewHolder>(data: MutableList<T>? = null)
    : BaseQuickAdapter<T, VH>(0, data) {

    companion object {
        const val TYPE_NOT_FOUND = -404
    }

    private val layouts: SparseIntArray by lazy(LazyThreadSafetyMode.NONE) { SparseIntArray() }

    override fun getDefItemViewType(position: Int): Int {
        return data[position].itemType
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VH {
        return createBaseViewHolder(parent, layouts.get(viewType, TYPE_NOT_FOUND))
    }

    /**
     * 调用此方法，设置多布局
     * @param type Int
     * @param layoutResId Int
     */
    protected fun addItemType(type: Int, @LayoutRes layoutResId: Int) {
        layouts.put(type, layoutResId)
    }
}