package com.chad.baserecyclerviewadapterhelper.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.chad.library.adapter.base.animation.ItemAnimation;

import org.jetbrains.annotations.NotNull;

public class CustomAnimation3 implements ItemAnimation {

    @NotNull
    @Override
    public Animator animator(@NotNull View view) {
        Animator alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1f);

        Animator translationY =
                ObjectAnimator.ofFloat(view, "translationY", view.getRootView().getHeight(), 0f);
        translationY.setInterpolator(new DecelerateInterpolator(1.2f));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(450);
        animatorSet.play(alpha).with(translationY);

        return animatorSet;
    }
}
