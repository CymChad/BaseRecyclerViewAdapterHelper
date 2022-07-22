package com.chad.library.adapter.base.loadState.leading

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.loadState.LoadState
import com.chad.library.databinding.BrvahLeadingLoadMoreBinding

/**
 * Default leading load state adapter
 *
 * 默认实现的尾部"向上加载更多" Adapter
 */
internal class DefaultLeadingLoadStateAdapter :LeadingLoadStateAdapter<DefaultLeadingLoadStateAdapter.LeadingLoadStateVH>() {

    /**
     * Default ViewHolder
     *
     * 默认实现的 ViewHolder
     */
    internal class LeadingLoadStateVH(
        parent: ViewGroup,
        val viewBinding: BrvahLeadingLoadMoreBinding = BrvahLeadingLoadMoreBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(viewBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LeadingLoadStateVH {
        return LeadingLoadStateVH(parent)
    }

    override fun onBindViewHolder(holder: LeadingLoadStateVH, loadState: LoadState) {
        if (loadState is LoadState.Loading) {
            holder.viewBinding.loadingProgress.visibility = View.VISIBLE
        } else {
            holder.viewBinding.loadingProgress.visibility = View.GONE
        }
    }
}