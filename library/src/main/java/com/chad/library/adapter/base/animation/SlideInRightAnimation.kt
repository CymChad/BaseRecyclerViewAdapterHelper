package com.chad.library.adapter.base.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
class SlideInRightAnimation : BaseAnimation {
    override fun animators(view: View): Array<Animator> {
        val animator = ObjectAnimator.ofFloat(view, "translationX", view.rootView.width.toFloat(), 0f)
        animator.duration = 300L
        animator.interpolator = DecelerateInterpolator()
        return arrayOf(animator)
    }
}