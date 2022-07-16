package com.chad.baserecyclerviewadapterhelper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.baserecyclerviewadapterhelper.databinding.ViewLoadMoreBinding
import com.chad.library.adapter.base.loadState.LoadState
import com.chad.library.adapter.base.loadState.trailing.TrailingLoadStateAdapter

/**
 * 自定义的"加载更多"
 *
 * 这里可以做很多事情，这里仅展示了更改自定义布局的使用
 */
class CustomLoadMoreAdapter : TrailingLoadStateAdapter() {

    override fun onCreateViewHolder(
        context: Context, parent: ViewGroup, loadState: LoadState
    ): TrailingViewHolder {
        val viewBinding = ViewLoadMoreBinding.inflate(LayoutInflater.from(context), parent, false)
        return CustomVH(viewBinding)
    }

    class CustomVH(viewBinding: ViewLoadMoreBinding) :
        TrailingLoadStateAdapter.TrailingViewBindingVH<ViewLoadMoreBinding>(viewBinding) {

        override fun getLoadComplete(viewBinding: ViewLoadMoreBinding): View {
            return viewBinding.loadMoreLoadCompleteView
        }

        override fun getLoadingView(viewBinding: ViewLoadMoreBinding): View {
            return viewBinding.loadMoreLoadingView
        }

        override fun getLoadEndView(viewBinding: ViewLoadMoreBinding): View {
            return viewBinding.loadMoreLoadEndView
        }

        override fun getLoadFailView(viewBinding: ViewLoadMoreBinding): View {
            return viewBinding.loadMoreLoadFailView
        }
    }
}