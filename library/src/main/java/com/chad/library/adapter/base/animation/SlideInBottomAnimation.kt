package com.chad.library.adapter.base.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
class SlideInBottomAnimation @JvmOverloads constructor(
    private val duration: Long = 400L,
) : ItemAnimator {

    private val interpolator = DecelerateInterpolator(1.3f)

    override fun animator(view: View): Animator {
        val animator = ObjectAnimator.ofFloat(view, "translationY", view.measuredHeight.toFloat(), 0f)
        animator.duration = duration
        animator.interpolator = interpolator
        return animator
    }
}