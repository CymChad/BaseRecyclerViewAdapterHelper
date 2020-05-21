package com.chad.baserecyclerviewadapterhelper.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable dividerDrawable;
    private int orientation = LinearLayoutManager.VERTICAL;

    public GridItemDecoration(Drawable divider) {
        dividerDrawable = divider;
    }

    public GridItemDecoration(Context context, int resId) {
        dividerDrawable = context.getResources().getDrawable(resId);
    }

    public GridItemDecoration(Context context, int resId, int orientation) {
        dividerDrawable = context.getResources().getDrawable(resId);
        this.orientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (dividerDrawable == null) {
            return;
        }

        if (parent.getChildLayoutPosition(view) < 1) {
            return;
        }

        if (orientation == LinearLayoutManager.VERTICAL) {
            outRect.top = dividerDrawable.getIntrinsicHeight();
        } else if (orientation == LinearLayoutManager.HORIZONTAL) {
            outRect.left = dividerDrawable.getIntrinsicWidth();
        }
    }

    /**
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (dividerDrawable == null) {
            return;
        }

        int childCount = parent.getChildCount();
        int rightV = parent.getWidth();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int leftV = parent.getPaddingLeft() + child.getPaddingLeft();
            int bottomV = child.getTop() - params.topMargin;
            int topV = bottomV - dividerDrawable.getIntrinsicHeight();

            int topH = child.getTop() + params.topMargin;
            int bottomH = child.getBottom() + params.bottomMargin;
            int rightH = child.getLeft() - params.leftMargin;
            int leftH = rightH - dividerDrawable.getIntrinsicWidth();
            dividerDrawable.setBounds(leftH, topH, rightH, bottomH);
            dividerDrawable.draw(c);
            dividerDrawable.setBounds(leftV, topV, rightV, bottomV);
            dividerDrawable.draw(c);
        }
    }


}
