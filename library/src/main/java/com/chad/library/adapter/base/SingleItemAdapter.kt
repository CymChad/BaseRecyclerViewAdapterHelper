package com.chad.library.adapter.base

import androidx.recyclerview.widget.RecyclerView

abstract class SingleItemAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private var mItem: T? = null

    abstract fun onBindViewHolder(holder: VH, item: T?)

    open fun onBindViewHolder(holder: VH, item: T?, payloads: MutableList<Any>) {
        onBindViewHolder(holder, item)
    }

    final override fun getItemCount(): Int {
        return 1
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, mItem)
    }

    final override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
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
}