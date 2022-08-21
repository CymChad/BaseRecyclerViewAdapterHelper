package com.chad.baserecyclerviewadapterhelper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.library.adapter.base.fullspan.FullSpanAdapterType

class HomeTopHeaderAdapter : RecyclerView.Adapter<HomeTopHeaderAdapter.VH>(), FullSpanAdapterType {

    companion object {
        val HEAD_VIEWTYPE = 0x10000556
    }

    class VH(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.top_view, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return HEAD_VIEWTYPE
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
    }

    override fun getItemCount(): Int = 1


}