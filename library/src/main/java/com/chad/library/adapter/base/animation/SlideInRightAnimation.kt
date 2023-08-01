package com.chad.library.adapter.base.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * 让 item 从右侧滑入的动画
 * An animation to let items slide in from the right.(Using a DecelerateInterpolator with 1.8 factor.) Default duration is 400ms.
 */
class SlideInRightAnimation @JvmOverloads constructor(
    private val duration: Long = 400L,
) : ItemAnimator {
    private val interpolator = DecelerateInterpolator(1.8f)

    override fun animator(view: View): Animator {
        val animator = ObjectAnimator.ofFloat(view, "translationX", view.rootView.width.toFloat(), 0f)
        animator.duration = duration
        animator.interpolator = interpolator
        return animator
    }
}