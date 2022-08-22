package com.chad.baserecyclerviewadapterhelper.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.chad.library.adapter.base.dragswipe.listener.DragAndSwipeDataCallback

/**
 * kotlin方式集成案例
 */
open class HeaderDragAndSwipeAdapter : BaseQuickAdapter<String, QuickViewHolder>(),
    DragAndSwipeDataCallback {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_draggable_view, parent)
    }

     override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: String) {
        when (holder.layoutPosition % 3) {
            0 -> holder.setImageResource(R.id.iv_head, R.mipmap.head_img0)
            1 -> holder.setImageResource(R.id.iv_head, R.mipmap.head_img1)
            2 -> holder.setImageResource(R.id.iv_head, R.mipmap.head_img2)
            else -> {}
        }
        holder.setText(R.id.tv, item)
    }

    override fun dataSwap(fromPosition: Int, toPosition: Int) {
        swap(fromPosition, toPosition)
    }

    override fun dataRemoveAt(position: Int) {
        removeAt(position)
    }
}