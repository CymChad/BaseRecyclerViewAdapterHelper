package com.chad.baserecyclerviewadapterhelper.adapter.diffUtil

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.chad.library.adapter.base.dragswipe.DragAndSwipeAdapterImpl

/**
 * kotlin方式集成案例
 */
class DragAndSwipeAdapter2 : BaseQuickAdapter<String, QuickViewHolder>(),
    DragAndSwipeAdapterImpl {

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

    override fun getDragAndSwipeAdapter(): RecyclerView.Adapter<*> {
        return this
    }

    override fun getDragAndSwipeData(): List<*> {
        return items
    }

}