package com.chad.library.adapter.base.animation;

import android.animation.ObjectAnimator;
import android.view.View;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class SlideInRightAnimation implements BaseAnimation {

    @Override public void startAnimator(View view) {
        ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0).setDuration(DEFAULT_DURATION).start();
    }
}
