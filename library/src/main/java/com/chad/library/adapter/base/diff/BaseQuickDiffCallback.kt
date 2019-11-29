package com.chad.library.adapter.base.diff

import androidx.recyclerview.widget.DiffUtil

/**
 * @author: limuyang
 * @date: 2019-11-29
 * @Description: Diff Callback基类
 */
abstract class BaseQuickDiffCallback<T>(newList: MutableList<T>?) : DiffUtil.Callback() {
    internal val newList: MutableList<T> = newList ?: arrayListOf()

    private lateinit var oldList: MutableList<T>

    internal fun setOldList(list: MutableList<T>?) {
        oldList = list ?: arrayListOf()
    }

    override fun getNewListSize(): Int = newList.size

    override fun getOldListSize(): Int = oldList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }


    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return getChangePayload(oldList[oldItemPosition], newList[newItemPosition])
    }

    /**
     * @param oldItem New data
     * @param newItem old Data
     * @return Return false if items are no same
     */
    protected abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    /**
     * @param oldItem New data
     * @param newItem old Data
     * @return Return false if item content are no same
     */
    protected abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    /**
     * Optional implementation
     *
     * @param oldItem New data
     * @param newItem old Data
     * @return Payload info
     */
    protected open fun getChangePayload(oldItem: T, newItem: T): Any? {
        return null
    }
}