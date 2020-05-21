package com.chad.baserecyclerviewadapterhelper.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.chad.library.adapter.base.animation.BaseAnimation;

import org.jetbrains.annotations.NotNull;

public class CustomAnimation3 implements BaseAnimation {

    @NotNull
    @Override
    public Animator[] animators(@NotNull View view) {
        Animator alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1f);
        alpha.setDuration(450);

        Animator translationY =
                ObjectAnimator.ofFloat(view, "translationY", view.getRootView().getHeight(), 0f);
        translationY.setDuration(450);
        translationY.setInterpolator(new DecelerateInterpolator(1.2f));

        return new Animator[]{alpha, translationY};
    }
}
