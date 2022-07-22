package com.chad.baserecyclerviewadapterhelper.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Interpolator;

import com.chad.library.adapter.base.animation.ItemAnimation;

import org.jetbrains.annotations.NotNull;

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.sin;


/**
 * 自定义动画2
 */
public class CustomAnimation2 implements ItemAnimation {
    @NotNull
    @Override
    public Animator animator(@NotNull View view) {
        Animator translationX =
                ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0f);

        translationX.setDuration(800);
        translationX.setInterpolator(new MyInterpolator2());

        return translationX;
    }

    private static class MyInterpolator2 implements Interpolator {
        @Override
        public float getInterpolation(float input) {
            float factor = 0.7f;
            return (float) (pow(2.0, -10.0 * input) * sin((input - factor / 4) * (2 * PI) / factor) + 1);
        }
    }
}