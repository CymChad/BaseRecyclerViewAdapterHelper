package com.chad.library.adapter.base.loadmore

import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.viewholder.QuickViewHolder

/**
 *
 * @author limuyang
 */

enum class LoadMoreStatus {
    Complete, Loading, Fail, End
}

/**
 * 继承此类，实行自定义loadMore视图
 */
abstract class BaseLoadMoreView {

    /**
     * 根布局
     * @param parent ViewGroup
     * @return View
     */
    abstract fun getRootView(parent: ViewGroup): View

    /**
     * 布局中的 加载更多视图
     * @param holder BaseViewHolder
     * @return View
     */
    abstract fun getLoadingView(holder: QuickViewHolder): View

    /**
     * 布局中的 加载完成布局
     * @param holder BaseViewHolder
     * @return View
     */
    abstract fun getLoadComplete(holder: QuickViewHolder): View

    /**
     * 布局中的 加载结束布局
     * @param holder BaseViewHolder
     * @return View
     */
    abstract fun getLoadEndView(holder: QuickViewHolder): View

    /**
     * 布局中的 加载失败布局
     * @param holder BaseViewHolder
     * @return View
     */
    abstract fun getLoadFailView(holder: QuickViewHolder): View

    /**
     * 可重写此方式，实行自定义逻辑
     * @param holder BaseViewHolder
     * @param position Int
     * @param loadMoreStatus LoadMoreStatus
     */
    open fun convert(holder: QuickViewHolder, position: Int, loadMoreStatus: LoadMoreStatus) {
        when (loadMoreStatus) {
            LoadMoreStatus.Complete -> {
                getLoadingView(holder).isVisible(false)
                getLoadComplete(holder).isVisible(true)
                getLoadFailView(holder).isVisible(false)
                getLoadEndView(holder).isVisible(false)
            }
            LoadMoreStatus.Loading -> {
                getLoadingView(holder).isVisible(true)
                getLoadComplete(holder).isVisible(false)
                getLoadFailView(holder).isVisible(false)
                getLoadEndView(holder).isVisible(false)
            }
            LoadMoreStatus.Fail -> {
                getLoadingView(holder).isVisible(false)
                getLoadComplete(holder).isVisible(false)
                getLoadFailView(holder).isVisible(true)
                getLoadEndView(holder).isVisible(false)
            }
            LoadMoreStatus.End -> {
                getLoadingView(holder).isVisible(false)
                getLoadComplete(holder).isVisible(false)
                getLoadFailView(holder).isVisible(false)
                getLoadEndView(holder).isVisible(true)
            }
        }
    }

    private fun View.isVisible(visible: Boolean) {
        this.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}

