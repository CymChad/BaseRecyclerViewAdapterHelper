package com.chad.baserecyclerviewadapterhelper.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.chad.library.adapter.base.animation.BaseAnimation;

/**
 * 项目名称：BaseRecyclerViewAdapterHelper
 * 类描述：
 * 创建人：Chad
 * 创建时间：16/4/14 上午10:34
 */
public class CustomAnimation extends BaseAnimation {

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleY", 1, 1.1f, 1),
                ObjectAnimator.ofFloat(view, "scaleX", 1, 1.1f, 1)
        };
    }
}
