package com.chad.library.adapter.base

import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

/**
 * Base Class
 * @param T : type of data, 数据类型
 * @param VH : BaseViewHolder
 * @constructor layoutId, data(Can null parameters, the default is empty data)
 */
abstract class BaseQuickAdapter<T, VH : RecyclerView.ViewHolder>(items: List<T> = emptyList()) :
    BrvahAdapter<T, VH>(items) {


    /*************************** 设置数据相关 ******************************************/

    /**
     * setting up a new instance to data;
     *
     * 使用新的数据集合，改变原有数据集合内容。
     * 注意：不会替换原有的内存引用，只是替换内容
     *
     * @param list Collection<T>?
     */
    fun setList(list: List<T>?) {
        if (list === this.items) {
            return
        }

        this.items = list ?: emptyList()
        mLastPosition = -1
        notifyDataSetChanged()
    }

    /**
     * change data
     * 改变某一位置数据
     */
    operator fun set(@IntRange(from = 0) position: Int, data: T) {
        if (position >= this.items.size) {
            return
        }

        if (items is MutableList) {
            (items as MutableList<T>)[position] = data
            notifyItemChanged(position)
        }
    }

    /**
     * add one new data in to certain location
     * 在指定位置添加一条新数据
     *
     * @param position
     */
    fun add(@IntRange(from = 0) position: Int, data: T) {
        if (items is MutableList) {
            (items as MutableList<T>).add(position, data)
            notifyItemInserted(position)
        }

//        compatibilityDataSizeChanged(1)
    }

    /**
     * add one new data
     * 添加一条新数据
     */
    fun add(@NonNull data: T) {
        if (items is MutableList) {
            (items as MutableList<T>).add(data)
            notifyItemInserted(items.size)
        }

//        compatibilityDataSizeChanged(1)
    }

    /**
     * add new data in to certain location
     * 在指定位置添加数据
     *
     * @param position the insert position
     * @param newData  the new data collection
     */
    fun add(@IntRange(from = 0) position: Int, newData: Collection<T>) {
        if (items is MutableList) {
            (items as MutableList<T>).addAll(position, newData)
            notifyItemRangeInserted(position, newData.size)
        }

//        compatibilityDataSizeChanged(newData.size)
    }

    fun addAll(@NonNull newData: Collection<T>) {
        if (items is MutableList) {
            val oldSize = items.size
            (items as MutableList<T>).addAll(newData)
            notifyItemRangeInserted(oldSize, newData.size)
        }

//        compatibilityDataSizeChanged(newData.size)
    }

    /**
     * remove the item associated with the specified position of adapter
     * 删除指定位置的数据
     *
     * @param position
     */
    fun removeAt(@IntRange(from = 0) position: Int) {
        if (position >= items.size) {
            return
        }

        if (items is MutableList) {
            (items as MutableList<T>).removeAt(position)
            notifyItemRemoved(position)
        }

//        compatibilityDataSizeChanged(0)
//        notifyItemRangeChanged(position, this.data.size - position)
    }

    fun remove(data: T) {
        val index = items.indexOf(data)
        if (index == -1) {
            return
        }
        removeAt(index)
    }


    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     *
     * @param size Need compatible data size
     */
    protected fun compatibilityDataSizeChanged(size: Int) {
        if (items.size == size) {
            notifyDataSetChanged()
        }
    }


}
