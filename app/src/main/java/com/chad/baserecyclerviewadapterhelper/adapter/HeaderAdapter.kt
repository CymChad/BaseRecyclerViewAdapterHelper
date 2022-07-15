package com.chad.baserecyclerviewadapterhelper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.baserecyclerviewadapterhelper.R

class HeaderAdapter(private val click: View.OnClickListener): RecyclerView.Adapter<HeaderAdapter.VH>() {

    class VH(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.head_view, parent, false)
        return VH(view).apply {
            itemView.setOnClickListener(click)
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
    }

    override fun getItemCount(): Int {
        return 1
    }
}