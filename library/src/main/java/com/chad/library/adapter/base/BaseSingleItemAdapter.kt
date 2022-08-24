package com.chad.library.adapter.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for single item
 * 只有单个 item 情况下的 Adapter
 *
 * @param T 数据类型 type of data
 * @param VH viewHolder类型 type of the viewHolder
 * @property mItem 数据  data
 * @constructor Create empty Single item adapter
 */
abstract class BaseSingleItemAdapter<T, VH : RecyclerView.ViewHolder>(private var mItem: T? = null) :
    RecyclerView.Adapter<VH>() {

    private var mOnItemClickListener: ((BaseSingleItemAdapter<T, *>, view: View, item: T?) -> Unit)? = null
    private var mOnItemLongClickListener: ((BaseSingleItemAdapter<T, *>, view: View, item: T?) -> Boolean)? = null

    protected abstract fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int
    ): VH

    protected abstract fun onBindViewHolder(holder: VH, item: T?)

    open fun onBindViewHolder(holder: VH, item: T?, payloads: MutableList<Any>) {
        onBindViewHolder(holder, item)
    }

    final override fun getItemCount(): Int {
        return 1
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateViewHolder(parent.context, parent, viewType).apply {
            mOnItemClickListener?.let {
                itemView.setOnClickListener { v ->
                    it(this@BaseSingleItemAdapter, v, mItem)
                }
            }
            mOnItemLongClickListener?.let {
                itemView.setOnLongClickListener { v ->
                    it(this@BaseSingleItemAdapter, v, mItem)
                }
            }
        }
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, mItem)
    }

    final override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, mItem)
            return
        }
        onBindViewHolder(holder, mItem, payloads)
    }

    fun setItem(t: T?, payload: Any?) {
        mItem = t
        notifyItemChanged(0, payload)
    }

    var item: T?
        get() = mItem
        set(value) {
            mItem = value
            notifyItemChanged(0)
        }

    fun setOnItemClickListener(listener: ((BaseSingleItemAdapter<T, *>, view: View, item: T?) -> Unit)?) =
        apply {
            this.mOnItemClickListener = listener
        }

    fun getOnItemClickListener() = mOnItemClickListener

    fun setOnItemLongClickListener(listener: ((BaseSingleItemAdapter<T, *>, view: View, item: T?) -> Boolean)?) {
        this.mOnItemLongClickListener = listener
    }

    fun getOnItemLongClickListener() = mOnItemLongClickListener

}