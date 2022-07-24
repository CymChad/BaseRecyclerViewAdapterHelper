package com.chad.library.adapter.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 只有单个 item 情况下的 Adapter
 *
 * @param T 数据类型
 * @param VH viewHolder类型
 * @property mItem 数据
 * @constructor Create empty Single item adapter
 */
abstract class SingleItemAdapter<T, VH : RecyclerView.ViewHolder>(private var mItem: T? = null) : RecyclerView.Adapter<VH>() {

    private var mOnItemClickListener: OnItemClickListener<T>? = null
    private var mOnItemLongClickListener: OnItemLongClickListener<T>? = null

    protected abstract fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int
    ): VH

    protected  abstract fun onBindViewHolder(holder: VH, item: T?)

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
                   it.onItemClick(this@SingleItemAdapter, v, mItem)
               }
           }
           mOnItemLongClickListener?.let {
               itemView.setOnLongClickListener { v ->
                   it.onItemLongClick(this@SingleItemAdapter, v, mItem)
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

    fun setOnItemClickListener(listener: OnItemClickListener<T>?) {
        this.mOnItemClickListener = listener
    }

    fun getOnItemClickListener(): OnItemClickListener<T>? = mOnItemClickListener

    fun setOnItemLongClickListener(listener: OnItemLongClickListener<T>?) {
        this.mOnItemLongClickListener = listener
    }

    fun getOnItemLongClickListener(): OnItemLongClickListener<T>? = mOnItemLongClickListener


    fun interface OnItemClickListener<T> {
        fun onItemClick(adapter: SingleItemAdapter<T, *>, view: View, item: T?)
    }

    fun interface OnItemLongClickListener<T> {
        fun onItemLongClick(adapter: SingleItemAdapter<T, *>, view: View, item: T?): Boolean
    }
}