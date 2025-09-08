package com.chad.library.adapter4

import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for single item
 * 只有单个/一个 item 情况下的 Adapter
 *
 * @param T 数据类型 type of data
 * @param VH viewHolder类型 type of the viewHolder
 * @property mItem 数据 data
 */
abstract class BaseSingleItemAdapter<T : Any, VH : RecyclerView.ViewHolder>(private var mItem: T? = null) :
    BaseQuickAdapter<Any, VH>() {

    protected abstract fun onBindViewHolder(holder: VH, item: T?)

    open fun onBindViewHolder(holder: VH, item: T?, payloads: List<Any>) {
        onBindViewHolder(holder, item)
    }

    final override fun onBindViewHolder(holder: VH, position: Int, item: Any?) {
        onBindViewHolder(holder, mItem)
    }

    final override fun onBindViewHolder(holder: VH, position: Int, item: Any?, payloads: List<Any>) {
        onBindViewHolder(holder, mItem, payloads)
    }

    final override fun getItemCount(items: List<Any>): Int {
        return 1
    }

    /**
     * 设置 item 数据（payload 方式）
     *
     * @param t
     * @param payload
     */
    fun setItem(t: T?, payload: Any?) {
        mItem = t
        notifyItemChanged(0, payload)
    }

    /**
     * 获取/设置 item 数据
     */
    var item: T?
        get() = mItem
        set(value) {
            mItem = value
            notifyItemChanged(0)
        }

    override fun submitList(list: List<Any>?, commitCallback: Runnable?) {
        throw RuntimeException("Please use setItem()")
    }

    override fun add(data: Any, commitCallback: Runnable?) {
        throw RuntimeException("Please use setItem()")
    }

    override fun add(position: Int, data: Any, commitCallback: Runnable?) {
        throw RuntimeException("Please use setItem()")
    }

    override fun addAll(collection: Collection<Any>, commitCallback: Runnable?) {
        throw RuntimeException("Please use setItem()")
    }

    override fun addAll(position: Int, collection: Collection<Any>, commitCallback: Runnable?) {
        throw RuntimeException("Please use setItem()")
    }

    override fun remove(data: Any, commitCallback: Runnable?) {
        throw RuntimeException("Please use setItem()")
    }

    override fun removeAtRange(range: IntRange, commitCallback: Runnable?) {
        throw RuntimeException("Please use setItem()")
    }

    override fun removeAt(position: Int, commitCallback: Runnable?) {
        throw RuntimeException("Please use setItem()")
    }

    override fun set(position: Int, data: Any) {
        throw RuntimeException("Please use setItem()")
    }

    override fun set(position: Int, data: Any, commitCallback: Runnable?) {
        throw RuntimeException("Please use setItem()")
    }
}