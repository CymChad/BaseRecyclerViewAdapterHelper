package com.chad.library.adapter.base.animation;

import android.animation.ObjectAnimator;
import android.view.View;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class SlideInBottomAnimation implements BaseAnimation {

    @Override public void startAnimator(View view) {
        ObjectAnimator.ofFloat(view, "translationY", view.getMeasuredHeight(), 0).setDuration(DEFAULT_DURATION).start();
    }
}
