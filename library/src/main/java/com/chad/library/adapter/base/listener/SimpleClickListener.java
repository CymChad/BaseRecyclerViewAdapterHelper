package com.chad.library.adapter.base.listener;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Iterator;
import java.util.Set;

import static com.chad.library.adapter.base.BaseQuickAdapter.EMPTY_VIEW;
import static com.chad.library.adapter.base.BaseQuickAdapter.FOOTER_VIEW;
import static com.chad.library.adapter.base.BaseQuickAdapter.HEADER_VIEW;
import static com.chad.library.adapter.base.BaseQuickAdapter.LOADING_VIEW;

/**
 * Created by AllenCoder on 2016/8/03.
 *
 * This can be useful for applications that wish to implement various forms of click and longclick and childView click
 * manipulation of item views within the RecyclerView. SimpleClickListener may intercept
 * a touch interaction already in progress even if the SimpleClickListener is already handling that
 * gesture stream itself for the purposes of scrolling.
 *
 * @see RecyclerView.OnItemTouchListener
 */
public abstract class SimpleClickListener implements RecyclerView.OnItemTouchListener {
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;
    private Set<Integer> childClickViewIds;
    private Set<Integer> longClickViewIds;
    protected BaseQuickAdapter baseQuickAdapter;
    public static String TAG = "SimpleClickListener";

    /**
     * @param recyclerView     the parent recycleView
     * @param baseQuickAdapter this helper need the BaseQuickAdapter
     */

    public SimpleClickListener(RecyclerView recyclerView, BaseQuickAdapter baseQuickAdapter) {
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

                /**
                 *  have a headview and EMPTY_VIEW FOOTER_VIEW LOADING_VIEW
                 */
                int type= baseQuickAdapter.getItemViewType(vh.getLayoutPosition());
                if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW){
                    return false;
                }
                childClickViewIds = vh.getChildClickViewIds();

                if (childClickViewIds != null && childClickViewIds.size() > 0) {
                    for (Iterator it = childClickViewIds.iterator(); it.hasNext(); ) {
                        View childView = child.findViewById((Integer) it.next());
                        if (inRangeOfView(childView, e)) {
                            onItemChildClick(baseQuickAdapter,childView, vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount());
                            return true;
                        }
                    }


                    onItemClick(baseQuickAdapter,child, vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount());
                } else {
                    onItemClick(baseQuickAdapter,child, vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount());
                }


            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                BaseViewHolder vh = (BaseViewHolder) recyclerView.getChildViewHolder(child);

                longClickViewIds =vh.getItemChildLongClickViewIds();
                if (longClickViewIds!=null&&longClickViewIds.size()>0){
                    for (Iterator it = longClickViewIds.iterator(); it.hasNext(); ) {
                        View childView = child.findViewById((Integer) it.next());
                        if (inRangeOfView(childView, e)) {
                            onItemChildLongClick(baseQuickAdapter,childView, vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount());
                            return ;
                        }
                    }
                }
                onItemLongClick(baseQuickAdapter,child, vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount());
            }
        }


    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     *
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     */
    public abstract void onItemClick(BaseQuickAdapter adapter,View view, int position);

    /**
     * callback method to be invoked when an item in this view has been
     * click and held
     *
     * @param view     The view whihin the AbsListView that was clicked
     * @param position The position of the view int the adapter
     * @return true if the callback consumed the long click ,false otherwise
     */
    public abstract void onItemLongClick(BaseQuickAdapter adapter,View view, int position);
    public abstract void onItemChildClick(BaseQuickAdapter adapter, View view, int position);
    public abstract void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position);

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


