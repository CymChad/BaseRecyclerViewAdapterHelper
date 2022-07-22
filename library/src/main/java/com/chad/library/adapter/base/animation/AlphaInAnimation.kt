package com.chad.library.adapter.base.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
class AlphaInAnimation @JvmOverloads constructor(
    private val duration: Long = 300,
    private val mFrom: Float = DEFAULT_ALPHA_FROM
) : ItemAnimation {

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