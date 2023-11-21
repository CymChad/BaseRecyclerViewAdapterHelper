package com.chad.library.adapter4.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * item 缩放进入的动画
 * An animation to scale item in, changing item's scaleX and scaleY from default 0.5f to 1.0f in default 300ms.(Using a DecelerateInterpolator with default factor.)
 */
class ScaleInAnimation @JvmOverloads constructor(
    private val duration: Long = 300,
    private val mFrom: Float = DEFAULT_SCALE_FROM
) : ItemAnimator {

    private val interpolator = DecelerateInterpolator()

    override fun animator(view: View): Animator {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f)

        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.duration = duration
        animatorSet.interpolator = interpolator
        animatorSet.play(scaleX).with(scaleY)

        return animatorSet
    }

    companion object {
        private const val DEFAULT_SCALE_FROM = .5f
    }
}