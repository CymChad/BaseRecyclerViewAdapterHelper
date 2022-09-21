package com.chad.library.adapter.base.loadState.leading

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.R
import com.chad.library.adapter.base.loadState.LoadState

/**
 * Default leading load state adapter
 *
 * 默认实现的尾部"向上加载更多" Adapter
 */
internal class DefaultLeadingLoadStateAdapter :
    LeadingLoadStateAdapter<DefaultLeadingLoadStateAdapter.LeadingLoadStateVH>() {

    /**
     * Default ViewHolder
     *
     * 默认实现的 ViewHolder
     */
    internal class LeadingLoadStateVH(
        parent: ViewGroup,
        view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.brvah_leading_load_more, parent, false)
    ) : RecyclerView.ViewHolder(view) {
        val loadingProgress: View = itemView.findViewById(R.id.loading_progress)
    }


    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LeadingLoadStateVH {
        return LeadingLoadStateVH(parent)
    }

    override fun onBindViewHolder(holder: LeadingLoadStateVH, loadState: LoadState) {
        if (loadState is LoadState.Loading) {
            holder.loadingProgress.visibility = View.VISIBLE
        } else {
            holder.loadingProgress.visibility = View.GONE
        }
    }

    override fun getStateViewType(loadState: LoadState): Int = R.layout.brvah_leading_load_more
}