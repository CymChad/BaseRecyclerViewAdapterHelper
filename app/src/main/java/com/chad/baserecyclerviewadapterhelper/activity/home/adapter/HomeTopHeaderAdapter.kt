package com.chad.baserecyclerviewadapterhelper.activity.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.library.adapter.base.SimpleSingleItemAdapter
import com.chad.library.adapter.base.fullspan.FullSpanAdapterType

class HomeTopHeaderAdapter : SimpleSingleItemAdapter<HomeTopHeaderAdapter.VH>(),
    FullSpanAdapterType {

    companion object {
        val HEAD_VIEWTYPE = 0x10000556
    }

    class VH(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.top_view, parent, false))
    }

    override fun getItemViewType(position: Int, list: List<Any?>): Int {
        return HEAD_VIEWTYPE
    }


}