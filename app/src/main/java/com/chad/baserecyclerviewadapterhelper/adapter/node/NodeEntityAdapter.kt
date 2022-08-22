package com.chad.baserecyclerviewadapterhelper.adapter.node

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.baserecyclerviewadapterhelper.databinding.ItemNodeEntityBinding
import com.chad.baserecyclerviewadapterhelper.entity.NodeEntity
import com.chad.library.adapter.base.BaseSingleItemAdapter

class NodeEntityAdapter(item: NodeEntity?) : BaseSingleItemAdapter<NodeEntity, NodeEntityAdapter.VH>(item) {

    class VH(val viewBinding: ItemNodeEntityBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(ItemNodeEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, item: NodeEntity?) {
        holder.viewBinding.apply {
            tvTitle.text = item?.title
        }
    }
}