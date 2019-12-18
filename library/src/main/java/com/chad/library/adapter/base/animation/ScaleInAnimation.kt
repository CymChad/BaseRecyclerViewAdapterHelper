package com.chad.library.adapter.base.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
class ScaleInAnimation @JvmOverloads constructor(private val mFrom: Float = DEFAULT_SCALE_FROM) : BaseAnimation {

    override fun animators(view: View): Array<Animator> {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f)
        scaleX.duration = 300L
        scaleX.interpolator = DecelerateInterpolator()

        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f)
        scaleY.duration = 300L
        scaleY.interpolator = DecelerateInterpolator()
        return arrayOf(scaleX, scaleY)
    }

    companion object {
        private const val DEFAULT_SCALE_FROM = .5f
    }
}