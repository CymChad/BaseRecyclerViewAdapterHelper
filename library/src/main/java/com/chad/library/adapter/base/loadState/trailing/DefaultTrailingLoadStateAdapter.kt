package com.chad.library.adapter.base.loadState.trailing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.R
import com.chad.library.adapter.base.loadState.LoadState
import com.chad.library.databinding.BrvahTrailingLoadMoreBinding

/**
 * Default trailing load state adapter
 *
 * 默认实现的尾部"加载更多" Adapter
 */
internal class DefaultTrailingLoadStateAdapter: TrailingLoadStateAdapter<DefaultTrailingLoadStateAdapter.TrailingLoadStateVH>() {

    /**
     * Default implementation of "load more" ViewHolder
     *
     * 默认实现的"加载更多" ViewHolder
     */
    internal class TrailingLoadStateVH(
        parent: ViewGroup,
        val viewBinding: BrvahTrailingLoadMoreBinding = BrvahTrailingLoadMoreBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder( parent: ViewGroup, loadState: LoadState): TrailingLoadStateVH {
        return TrailingLoadStateVH(parent).apply {
            viewBinding.loadMoreLoadFailView.setOnClickListener {
                // 失败重试点击事件
                invokeFailRetry()
            }
            viewBinding.loadMoreLoadCompleteView.setOnClickListener {
                // 加载更多，手动点击事件
                invokeLoadMore()
            }
        }
    }

    /**
     * bind LoadState
     *
     * 绑定加载状态
     */
    override fun onBindViewHolder(holder: TrailingLoadStateVH, loadState: LoadState) {
        when (loadState) {
            is LoadState.NotLoading -> {
                if (loadState.endOfPaginationReached) {
                    holder.viewBinding.loadMoreLoadCompleteView.visibility = View.GONE
                    holder.viewBinding.loadMoreLoadingView.visibility = View.GONE
                    holder.viewBinding.loadMoreLoadFailView.visibility = View.GONE
                    holder.viewBinding.loadMoreLoadEndView.visibility = View.VISIBLE
                } else {
                    holder.viewBinding.loadMoreLoadCompleteView.visibility = View.VISIBLE
                    holder.viewBinding.loadMoreLoadingView.visibility = View.GONE
                    holder.viewBinding.loadMoreLoadFailView.visibility = View.GONE
                    holder.viewBinding.loadMoreLoadEndView.visibility = View.GONE
                }
            }
            is LoadState.Loading -> {
                holder.viewBinding.loadMoreLoadCompleteView.visibility = View.GONE
                holder.viewBinding.loadMoreLoadingView.visibility = View.VISIBLE
                holder.viewBinding.loadMoreLoadFailView.visibility = View.GONE
                holder.viewBinding.loadMoreLoadEndView.visibility = View.GONE
            }
            is LoadState.Error -> {
                holder.viewBinding.loadMoreLoadCompleteView.visibility = View.GONE
                holder.viewBinding.loadMoreLoadingView.visibility = View.GONE
                holder.viewBinding.loadMoreLoadFailView.visibility = View.VISIBLE
                holder.viewBinding.loadMoreLoadEndView.visibility = View.GONE
            }
            is LoadState.None -> {
                holder.viewBinding.loadMoreLoadCompleteView.visibility = View.GONE
                holder.viewBinding.loadMoreLoadingView.visibility = View.GONE
                holder.viewBinding.loadMoreLoadFailView.visibility = View.GONE
                holder.viewBinding.loadMoreLoadEndView.visibility = View.GONE
            }
        }
    }

    override fun getStateViewType(loadState: LoadState): Int = R.layout.brvah_trailing_load_more
}