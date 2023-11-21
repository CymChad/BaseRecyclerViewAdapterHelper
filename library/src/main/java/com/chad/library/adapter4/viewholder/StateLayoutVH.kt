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
internal class StateLayoutVH constructor(
    parent: ViewGroup,
    stateView: View?,
    private val stateLayout: FrameLayout = FrameLayout(parent.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        setStateView(this, stateView)
    }
) : RecyclerView.ViewHolder(stateLayout), FullSpanAdapterType {


    fun changeStateView(stateView: View?) {
        setStateView(stateLayout, stateView)
    }

    companion object {
        private fun setStateView(rootView: ViewGroup, stateView: View?) {
            if (stateView == null) {
                rootView.removeAllViews()
                return
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
            }

            rootView.removeAllViews()
            rootView.addView(stateView)
        }
    }
}
