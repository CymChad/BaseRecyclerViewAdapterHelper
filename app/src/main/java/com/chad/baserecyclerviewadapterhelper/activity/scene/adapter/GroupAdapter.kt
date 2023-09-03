package com.chad.baserecyclerviewadapterhelper.activity.scene.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.databinding.ItemGroupTypeBinding
import com.chad.baserecyclerviewadapterhelper.entity.GroupDemoEntity
import com.chad.baserecyclerviewadapterhelper.utils.dp
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * 每一组的Adapter
 *
 */
class GroupAdapter : BaseQuickAdapter<GroupDemoEntity.Group, GroupAdapter.VH>(){

    class VH(
        parent: ViewGroup,
        val binding: ItemGroupTypeBinding = ItemGroupTypeBinding.inflate(LayoutInflater.from(parent.context), parent ,false)
    ):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: GroupDemoEntity.Group) {
        holder.binding.tvTitle.text = item.title
        holder.binding.tvContent.text = item.content

        holder.binding.lineView.isVisible = position > 0

        when (position) {
            0 -> {
                // 第一个item，设置上圆角背景
                holder.binding.root.setBackgroundResource(R.drawable.ic_group_item_top_bg)

                // 设置点间距
                holder.binding.root.updateLayoutParams<MarginLayoutParams> {
                    topMargin = 15.dp
                }
            }
            items.size - 1 -> {
                // 最后一个item，设置下圆角背景
                holder.binding.root.setBackgroundResource(R.drawable.ic_group_item_bottom_bg)
            }
            else -> {
                // 其他的，没有圆角的背景
                holder.binding.root.setBackgroundResource(R.drawable.ic_group_item_mid_bg)
            }
        }
    }
}