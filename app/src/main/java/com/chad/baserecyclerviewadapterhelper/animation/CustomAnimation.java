package com.chad.baserecyclerviewadapterhelper.animation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

import com.chad.library.adapter.base.animation.BaseAnimation;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class CustomAnimation implements BaseAnimation {

    @Override public void startAnimator(View view) {
        PropertyValuesHolder[] values = new PropertyValuesHolder[]{
                PropertyValuesHolder.ofFloat("scaleX", 1, 1.1f, 1),
                PropertyValuesHolder.ofFloat("scaleY", 1, 1.1f, 1)
        };
        ObjectAnimator.ofPropertyValuesHolder(view, values)
                .setDuration(DEFAULT_DURATION).start();
    }
}
