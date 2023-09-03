package com.chad.baserecyclerviewadapterhelper.activity.emptyview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.databinding.LayoutAnimationBinding
import com.chad.baserecyclerviewadapterhelper.entity.Status
import com.chad.library.adapter.base.BaseQuickAdapter

class EmptyViewAdapter : BaseQuickAdapter<Status, EmptyViewAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: LayoutAnimationBinding = LayoutAnimationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

     override fun onBindViewHolder(holder: VH, position: Int, item: Status) {
        when (holder.layoutPosition % 3) {
            0 -> holder.binding.img.setImageResource(R.mipmap.animation_img1)
            1 -> holder.binding.img.setImageResource(R.mipmap.animation_img2)
            2 -> holder.binding.img.setImageResource(R.mipmap.animation_img3)
            else -> {}
        }
        holder.binding.tweetName.text = "Hoteis in Rio de Janeiro"
        holder.binding.tweetText.text = "O ever youthful,O ever weeping"
    }

}