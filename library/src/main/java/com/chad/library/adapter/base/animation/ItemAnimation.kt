package com.chad.library.adapter.base.animation

import android.animation.Animator
import android.view.View

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
interface ItemAnimation {
    fun animator(view: View): Animator
}