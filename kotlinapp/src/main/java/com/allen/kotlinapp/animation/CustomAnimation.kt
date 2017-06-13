package com.allen.kotlinapp.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import com.chad.library.adapter.base.animation.BaseAnimation

/**
 * 文 件 名: CustomAnimation
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 14:31
 * 修改时间：
 * 修改备注：
 */
class CustomAnimation : BaseAnimation {

    override fun getAnimators(view: View): Array<Animator> {
       return arrayOf( ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f, 1f),
               ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f, 1f))

    }
}
