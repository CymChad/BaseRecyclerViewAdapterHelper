package com.chad.library.adapter.base.viewholder

import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView

internal class EmptyLayoutVH(private val emptyLayout:FrameLayout ): RecyclerView.ViewHolder(emptyLayout) {
    fun changeEmptyView(view: View) {
        val emptyLayoutVp: ViewParent? = view.parent
        if (emptyLayoutVp is ViewGroup) {
            emptyLayoutVp.removeView(view)
        }

        emptyLayout.removeAllViews()
        emptyLayout.addView(view)
    }
}
