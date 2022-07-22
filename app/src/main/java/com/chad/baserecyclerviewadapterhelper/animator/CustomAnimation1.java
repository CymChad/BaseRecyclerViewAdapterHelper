package com.chad.baserecyclerviewadapterhelper.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import com.chad.library.adapter.base.animation.ItemAnimator;
import org.jetbrains.annotations.NotNull;

/**
 * 自定义动画1
 */
public class CustomAnimation1 implements ItemAnimator {
    @NotNull
    @Override
    public Animator animator(@NotNull View view) {
        Animator alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1f);

        Animator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1);
        Animator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1);

        scaleY.setInterpolator(new DecelerateInterpolator());
        scaleX.setInterpolator(new DecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(350);
        animatorSet.play(alpha).with(scaleX).with(scaleY);

        return animatorSet;
    }
}