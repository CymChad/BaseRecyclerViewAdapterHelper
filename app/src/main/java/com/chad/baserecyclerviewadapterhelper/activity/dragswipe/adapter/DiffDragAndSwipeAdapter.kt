package com.chad.baserecyclerviewadapterhelper.activity.dragswipe.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.activity.differ.adapter.DiffEntityCallback
import com.chad.baserecyclerviewadapterhelper.entity.DiffEntity
import com.chad.library.adapter.base.BaseDifferAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder

/**
 * Create adapter
 */
class DiffDragAndSwipeAdapter :
    BaseDifferAdapter<DiffEntity, QuickViewHolder>(DiffEntityCallback()) {

    override fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.layout_animation, parent)
    }

    override fun onBindViewHolder(
        holder: QuickViewHolder, position: Int, item: DiffEntity?
    ) {
        holder.setText(R.id.tweetName, item!!.title)
            .setText(R.id.tweetText, item.content)
            .setText(R.id.tweetDate, item.date)
    }
}