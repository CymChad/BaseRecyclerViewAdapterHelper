package com.chad.baserecyclerviewadapterhelper.adapter

import android.view.ViewGroup
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.library.adapter.base.SingleItemAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder

class FooterAdapter(
    private val isDelete: Boolean,
    private val click: (FooterAdapter) -> Unit
) : SingleItemAdapter<Any, QuickViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.footer_view, parent).apply {
            itemView.setOnClickListener {
                click.invoke(this@FooterAdapter)
            }
        }
    }

    override fun onBindViewHolder(holder: QuickViewHolder, item: Any?) {
        if (isDelete) {
            holder.setImageResource(R.id.iv, R.mipmap.rm_icon)
        }
    }
}