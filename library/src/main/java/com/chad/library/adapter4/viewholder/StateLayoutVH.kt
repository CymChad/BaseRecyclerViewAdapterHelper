package com.chad.library.adapter4.viewholder

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.fullspan.FullSpanAdapterType

/**
 * An emptyState viewHolder. (For internal use only)
 * 内部使用的空状态ViewHolder
 *
 * @property stateLayout
 * @constructor Create empty Empty layout v h
 */
internal class StateLayoutVH(
    parent: ViewGroup,
    stateView: View?,
    isUseStateViewSize: Boolean,
    private val stateLayout: FrameLayout = FrameLayout(parent.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        setStateView(this, stateView, isUseStateViewSize)
    }
) : RecyclerView.ViewHolder(stateLayout), FullSpanAdapterType {


    fun changeStateView(stateView: View?, isUseStateViewSize: Boolean) {
        setStateView(stateLayout, stateView, isUseStateViewSize)
    }

    companion object {
        private fun setStateView(rootView: ViewGroup, stateView: View?, isUseStateViewSize: Boolean) {
            if (stateView == null) {
                rootView.removeAllViews()
                return
            }

            if (rootView.childCount == 1) {
                val old = rootView.getChildAt(0)
                if (old == stateView) {
                    // 如果是同一个view，不进行操作
                    return
                }
            }

            stateView.parent.run {
                if (this is ViewGroup) {
                    this.removeView(stateView)
                }
            }

            if (stateView.layoutParams == null) {
                stateView.layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER
                }
                rootView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                rootView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }else{
                if (
                    stateView.layoutParams is FrameLayout.LayoutParams
                    && ((stateView.layoutParams as FrameLayout.LayoutParams).gravity == Gravity.CENTER)
                    && stateView.layoutParams.width == FrameLayout.LayoutParams.WRAP_CONTENT
                    && stateView.layoutParams.height == FrameLayout.LayoutParams.WRAP_CONTENT){
                    //兼容stateview.layoutParams原本为空，设置FrameLayout.LayoutParams并居中的情况
                    rootView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    rootView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                }else{
                    //判断stateView宽度，如果不是MATCH_PARENT则把RootView的高度改为WRAP_CONTENT
                    if (stateView.layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT){
                        rootView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    }else{
                        rootView.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                    //判断stateView高度，如果不是MATCH_PARENT则把RootView的高度改为WRAP_CONTENT
                    if (stateView.layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT){
                        rootView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    }else{
                        rootView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                }
            }

            if (isUseStateViewSize) {
                val lp = rootView.layoutParams
                lp.height = stateView.layoutParams.height
                lp.width = stateView.layoutParams.width
                rootView.layoutParams = lp
            }

            rootView.removeAllViews()
            rootView.addView(stateView)
        }
    }
}
