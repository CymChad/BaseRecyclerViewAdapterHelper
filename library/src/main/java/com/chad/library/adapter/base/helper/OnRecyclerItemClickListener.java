package com.chad.library.adapter.base.helper;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by AllenCoder on 2016/8/03.
 *
 * This can be useful for applications that wish to implement various forms of click and longclick and childView click
 * manipulation of item views within the RecyclerView. OnRecyclerItemClickListener may intercept
 * a touch interaction already in progress even if the OnRecyclerItemClickListener is already handling that
 * gesture stream itself for the purposes of scrolling.
 *
 * @see RecyclerView.OnItemTouchListener
 */
public abstract class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;
    private Set<Integer> childClickViewIds;
    protected BaseQuickAdapter baseQuickAdapter;
    public static String TAG = "OnRecyclerItemClickListener";

    /**
     * @param recyclerView     the parent recycleView
     * @param baseQuickAdapter this helper need the BaseQuickAdapter
     */
    public OnRecyclerItemClickListener(RecyclerView recyclerView, BaseQuickAdapter baseQuickAdapter) {
        this.recyclerView = recyclerView;
        this.baseQuickAdapter = baseQuickAdapter;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

            if (child != null) {
                BaseViewHolder vh = (BaseViewHolder) recyclerView.getChildViewHolder(child);
                childClickViewIds = vh.getChildClickViewIds();

                if (childClickViewIds != null && childClickViewIds.size() > 0) {
                    for (Iterator it = childClickViewIds.iterator(); it.hasNext(); ) {
                        View childView = child.findViewById((Integer) it.next());
                        if (inRangeOfView(childView, e)) {
                            onItemChildClickHelper(childView, vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount());
                            return true;
                        }
                    }

                    onItemClickHelper(child, vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount());
                } else {
                    onItemClickHelper(child, vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount());
                }


            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                onItemLongClickHelper(child, vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount());
            }
        }


    }

    /**
     * @param view
     * @param position
     */
    public abstract void onItemClickHelper(View view, int position);

    public abstract void onItemLongClickHelper(View view, int position);

    public abstract void onItemChildClickHelper(View view, int position);


    public boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getRawX() < x
                || ev.getRawX() > (x + view.getWidth())
                || ev.getRawY() < y
                || ev.getRawY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }


}


