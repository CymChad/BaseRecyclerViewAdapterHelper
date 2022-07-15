package com.chad.baserecyclerviewadapterhelper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.chad.baserecyclerviewadapterhelper.R

class FooterAdapter(
    private val isDelete: Boolean,
    private val click: (FooterAdapter) -> Unit
) : RecyclerView.Adapter<FooterAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.footer_view, parent, false)
        return VH(view).apply {
            itemView.setOnClickListener {
                click.invoke(this@FooterAdapter)
            }
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (isDelete) {
            val imageView: ImageView = holder.itemView.findViewById(R.id.iv)
            imageView.setImageResource(R.mipmap.rm_icon)
        }
    }

    override fun getItemCount(): Int {
        return 1
    }
}