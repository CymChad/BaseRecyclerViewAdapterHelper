package com.chad.library.adapter4.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * An animation to fade item in, changing alpha from default 0f to 1.0f at a uniform rate in default 300ms.
 *
 * item 淡入显示的动画
 */
class AlphaInAnimation @JvmOverloads constructor(
    private val duration: Long = 300,
    private val mFrom: Float = DEFAULT_ALPHA_FROM
) : ItemAnimator {

    private val interpolator = LinearInterpolator()

    override fun animator(view: View): Animator {
        val animator = ObjectAnimator.ofFloat(view, "alpha", mFrom, 1f)
        animator.duration = duration
        animator.interpolator = interpolator
        return animator
    }

    companion object {
        private const val DEFAULT_ALPHA_FROM = 0f
    }

}