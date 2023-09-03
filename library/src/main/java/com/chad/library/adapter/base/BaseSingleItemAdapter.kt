package com.chad.library.adapter.base

import androidx.recyclerview.widget.RecyclerView

abstract class SimpleSingleItemAdapter<VH : RecyclerView.ViewHolder>() :
    BaseSingleItemAdapter<Any?, VH>(null) {

    protected abstract fun onBindViewHolder(holder: VH)
    final override fun onBindViewHolder(holder: VH, item: Any?) {
        onBindViewHolder(holder)
    }

    open fun onBindViewHolder(holder: VH, payloads: List<Any>) {
        onBindViewHolder(holder)
    }

    final override fun onBindViewHolder(holder: VH, item: Any?, payloads: List<Any>) {
        onBindViewHolder(holder, payloads)
    }
}

/**
 * Adapter for single item
 * 只有单个/一个 item 情况下的 Adapter
 *
 * @param T 数据类型 type of data
 * @param VH viewHolder类型 type of the viewHolder
 * @property mItem 数据 data
 */
abstract class BaseSingleItemAdapter<T, VH : RecyclerView.ViewHolder>(private var mItem: T) :
    BaseQuickAdapter<T, VH>() {

    protected abstract fun onBindViewHolder(holder: VH, item: T)

    open fun onBindViewHolder(holder: VH, item: T, payloads: List<Any>) {
        onBindViewHolder(holder, item)
    }

    final override fun onBindViewHolder(holder: VH, position: Int, item: T) {
        onBindViewHolder(holder, mItem)
    }

    final override fun onBindViewHolder(holder: VH, position: Int, item: T, payloads: List<Any>) {
        onBindViewHolder(holder, mItem, payloads)
    }

    final override fun getItemCount(items: List<T>): Int {
        return 1
    }

    override fun getItem(position: Int): T {
        return mItem
    }

    /**
     * 设置 item 数据（payload 方式）
     *
     * @param t
     * @param payload
     */
    fun setItem(t: T, payload: Any?) {
        mItem = t
        notifyItemChanged(0, payload)
    }

    /**
     * 获取/设置 item 数据
     */
    var item: T
        get() = mItem
        set(value) {
            mItem = value
            notifyItemChanged(0)
        }

    override fun submitList(list: List<T>?) {
        throw RuntimeException("Please use setItem()")
    }

    override fun add(data: T) {
        throw RuntimeException("Please use setItem()")
    }

    override fun add(position: Int, data: T) {
        throw RuntimeException("Please use setItem()")
    }

    override fun addAll(collection: Collection<T>) {
        throw RuntimeException("Please use setItem()")
    }

    override fun addAll(position: Int, collection: Collection<T>) {
        throw RuntimeException("Please use setItem()")
    }

    override fun remove(data: T) {
        throw RuntimeException("Please use setItem()")
    }

    override fun removeAt(position: Int) {
        throw RuntimeException("Please use setItem()")
    }

    override fun set(position: Int, data: T) {
        throw RuntimeException("Please use setItem()")
    }
}