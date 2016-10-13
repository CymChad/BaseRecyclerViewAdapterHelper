package com.chad.library.adapter.base.listener;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.vh.BaseViewHolder;

import java.util.HashSet;
import java.util.Iterator;

import static com.chad.library.adapter.base.BaseQuickAdapter.VIEW_TYPE_EMPTY_VIEW;
import static com.chad.library.adapter.base.BaseQuickAdapter.VIEW_TYPE_FOOTER_VIEW;
import static com.chad.library.adapter.base.BaseQuickAdapter.VIEW_TYPE_HEADER_VIEW;
import static com.chad.library.adapter.base.BaseQuickAdapter.VIEW_TYPE_LOADING_VIEW;

/**
 * Created by AllenCoder on 2016/8/03.
 * <p>
 * This can be useful for applications that wish to implement various forms of click and longclick and childView click
 * manipulation of item views within the RecyclerView. SimpleClickListener may intercept
 * a touch interaction already in progress even if the SimpleClickListener is already handling that
 * gesture stream itself for the purposes of scrolling.
 *
 * @see RecyclerView.OnItemTouchListener
 */
public abstract class SimpleClickListener extends RecyclerView.SimpleOnItemTouchListener {

    private GestureDetectorCompat mGestureDetector;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetectorCompat(rv.getContext(), new ItemTouchHelperGestureListener(rv, this));
        }
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    private static class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

        private RecyclerView mRecyclerView;
        private BaseQuickAdapter mBaseQuickAdapter;
        private SimpleClickListener mSimpleClickListener;

        public ItemTouchHelperGestureListener(RecyclerView recyclerView, SimpleClickListener simpleClickListener) {
            mRecyclerView = recyclerView;
            mBaseQuickAdapter = (BaseQuickAdapter) recyclerView.getAdapter();
            mSimpleClickListener = simpleClickListener;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            final View pressedView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            BaseViewHolder vh = (BaseViewHolder) mRecyclerView.getChildViewHolder(pressedView);
            if (!isDataViewPosition(vh)) {
                return false;
            }
            HashSet<Integer> childClickViewIds = vh.getChildClickViewIds();
            if (childClickViewIds != null && childClickViewIds.size() > 0) {
                for (Iterator<Integer> it = childClickViewIds.iterator(); it.hasNext(); ) {
                    View childView = vh.getView(it.next());
                    if (inRangeOfView(childView, e) && childView.isEnabled()) {
                        mSimpleClickListener.onItemChildClick(mBaseQuickAdapter, childView, vh.getLayoutPosition() - mBaseQuickAdapter.getHeaderViewCount());
                        return true;
                    }
                }
            }
            mSimpleClickListener.onItemClick(mBaseQuickAdapter, pressedView, vh.getLayoutPosition() - mBaseQuickAdapter.getHeaderViewCount());
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            final View pressedView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            BaseViewHolder vh = (BaseViewHolder) mRecyclerView.getChildViewHolder(pressedView);
            if (!isDataViewPosition(vh)) {
                return;
            }
            HashSet<Integer> longClickViewIds = vh.getItemChildLongClickViewIds();
            if (longClickViewIds != null && longClickViewIds.size() > 0) {
                for (Iterator<Integer> it = longClickViewIds.iterator(); it.hasNext(); ) {
                    View childView = vh.getView(it.next());
                    if (inRangeOfView(childView, e) && childView.isEnabled()) {
                        mSimpleClickListener.onItemChildLongClick(mBaseQuickAdapter, childView, vh.getLayoutPosition() - mBaseQuickAdapter.getHeaderViewCount());
                        return;
                    }
                }
            }
            mSimpleClickListener.onItemLongClick(mBaseQuickAdapter, pressedView, vh.getLayoutPosition() - mBaseQuickAdapter.getHeaderViewCount());
        }

        public boolean inRangeOfView(View view, MotionEvent ev) {
            int[] location = new int[2];
            if (view.getVisibility() != View.VISIBLE) {
                return false;
            }
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

        private boolean isDataViewPosition(BaseViewHolder vh) {
            int type = vh.getItemViewType();
            return !(type == VIEW_TYPE_EMPTY_VIEW
                    || type == VIEW_TYPE_HEADER_VIEW
                    || type == VIEW_TYPE_FOOTER_VIEW
                    || type == VIEW_TYPE_LOADING_VIEW);
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
    public abstract void onItemClick(BaseQuickAdapter adapter, View view, int position);

    /**
     * callback method to be invoked when an item in this view has been
     * click and held
     *
     * @param view     The view whihin the AbsListView that was clicked
     * @param position The position of the view int the adapter
     * @return true if the callback consumed the long click ,false otherwise
     */
    public abstract void onItemLongClick(BaseQuickAdapter adapter, View view, int position);

    public abstract void onItemChildClick(BaseQuickAdapter adapter, View view, int position);

    public abstract void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position);

}


