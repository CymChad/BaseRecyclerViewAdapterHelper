package com.chad.baserecyclerviewadapterhelper.activity.headerfooter.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.library.adapter.base.SimpleSingleItemAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder

class FooterAdapter(
    private val isDelete: Boolean
) : SimpleSingleItemAdapter<QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.footer_view, parent)
    }

    override fun onBindViewHolder(holder: QuickViewHolder) {
        if (isDelete) {
            holder.setImageResource(R.id.iv, R.mipmap.rm_icon)
        }
    }
}