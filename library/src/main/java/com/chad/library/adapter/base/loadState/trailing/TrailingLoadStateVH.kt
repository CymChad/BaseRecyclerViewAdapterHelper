package com.chad.library.adapter.base.loadState.trailing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.databinding.BrvahTrailingLoadMoreBinding

/**
 * 继承基类，实现默认的"加载更多"VH
 */
internal class TrailingLoadStateVH(
    parent: ViewGroup,
    viewBinding: BrvahTrailingLoadMoreBinding = BrvahTrailingLoadMoreBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : TrailingLoadStateAdapter.ViewBindingViewHolder<BrvahTrailingLoadMoreBinding>(
    viewBinding
) {

    override fun getLoadComplete(viewBinding: BrvahTrailingLoadMoreBinding): View {
        return viewBinding.loadMoreLoadCompleteView
    }

    override fun getLoadingView(viewBinding: BrvahTrailingLoadMoreBinding): View {
        return viewBinding.loadMoreLoadingView
    }

    override fun getLoadEndView(viewBinding: BrvahTrailingLoadMoreBinding): View {
        return viewBinding.loadMoreLoadEndView
    }

    override fun getLoadFailView(viewBinding: BrvahTrailingLoadMoreBinding): View {
        return viewBinding.loadMoreLoadFailView
    }
}