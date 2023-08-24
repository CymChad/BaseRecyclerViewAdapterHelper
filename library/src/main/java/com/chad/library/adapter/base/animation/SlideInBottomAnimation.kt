package com.chad.library.adapter.base.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * 让 item 从底部滑入的动画
 * An animation to let items slide in from the bottom.(Using a DecelerateInterpolator with 1.3 factor.) Default duration is 400ms.
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