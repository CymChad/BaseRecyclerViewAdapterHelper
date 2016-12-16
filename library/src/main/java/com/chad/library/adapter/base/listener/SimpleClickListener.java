package com.chad.library.adapter.base.listener;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

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
 * <p>
 * This can be useful for applications that wish to implement various forms of click and longclick and childView click
 * manipulation of item views within the RecyclerView. SimpleClickListener may intercept
 * a touch interaction already in progress even if the SimpleClickListener is already handling that
 * gesture stream itself for the purposes of scrolling.
 *
 * @see RecyclerView.OnItemTouchListener
 */
public abstract class SimpleClickListener implements RecyclerView.OnItemTouchListener {
    public static String TAG = "SimpleClickListener";
    protected BaseQuickAdapter baseQuickAdapter;
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;
    private Set<Integer> childClickViewIds;
    private Set<Integer> longClickViewIds;
    private boolean mIsPrepressed = false;
    private boolean mIsShowPress = false;
    private View mPressedView = null;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        if (recyclerView == null) {
            this.recyclerView = rv;
            this.baseQuickAdapter = (BaseQuickAdapter) recyclerView.getAdapter();
            ItemTouchHelperGestureListener gestureListener = new ItemTouchHelperGestureListener(recyclerView);
            mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), gestureListener);
        }
        if (!mGestureDetector.onTouchEvent(e) && e.getActionMasked() == MotionEvent.ACTION_UP && mIsShowPress) {
            if (mPressedView != null) {
                BaseViewHolder vh = (BaseViewHolder) recyclerView.getChildViewHolder(mPressedView);
                mPressedView.setPressed(vh != null && vh.getItemViewType() == LOADING_VIEW);
                mPressedView = null;
            }
            mIsShowPress = false;
            mIsPrepressed = false;


        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    private void setPressViewHotSpot(final MotionEvent e, final View mPressedView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /**
             * when   click   Outside the region  ,mPressedView is null
             */
            if (mPressedView != null && mPressedView.getBackground() != null) {
                Drawable drawable = mPressedView.getBackground();
                drawable.setHotspot(e.getRawX(), e.getY() - mPressedView.getY());
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

    private boolean isHeaderOrFooterPosition(int position) {
        /**
         *  have a headview and EMPTY_VIEW FOOTER_VIEW LOADING_VIEW
         */
        if (baseQuickAdapter == null) {
            if (recyclerView != null) {
                baseQuickAdapter = (BaseQuickAdapter) recyclerView.getAdapter();
            } else {
                return false;
            }
        }
        int type = baseQuickAdapter.getItemViewType(position);
        return (type == EMPTY_VIEW || type == HEADER_VIEW
                || type == FOOTER_VIEW || type == LOADING_VIEW);
    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

        private RecyclerView recyclerView;

        public ItemTouchHelperGestureListener(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            mIsPrepressed = true;
            mPressedView = recyclerView.findChildViewUnder(e.getX(), e.getY());

            super.onDown(e);
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            if (mIsPrepressed && mPressedView != null) {
//                mPressedView.setPressed(true);
                mIsShowPress = true;
            }
            super.onShowPress(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (!mIsPrepressed || mPressedView == null) {
                return true;
            }

            final View pressedView = mPressedView;
            BaseViewHolder vh = (BaseViewHolder) recyclerView.getChildViewHolder(pressedView);

            if (isHeaderOrFooterPosition(vh.getLayoutPosition())) {
                return false;
            }
            childClickViewIds = vh.getChildClickViewIds();

            if (childClickViewIds != null && childClickViewIds.size() > 0) {
                for (Iterator it = childClickViewIds.iterator(); it.hasNext(); ) {
                    View childView = pressedView.findViewById((Integer) it.next());
                    if (inRangeOfView(childView, e) && childView.isEnabled()) {
                        if (childView instanceof ViewGroup) {
                            childView = getViewClicked((ViewGroup) childView, e);
                        }
                        setPressViewHotSpot(e, childView);
                        childView.setPressed(true);
                        int position = vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount();
                        onItemChildClick(baseQuickAdapter, childView, position);
                        resetPressedView(childView);
                        return true;
                    } else {
                        childView.setPressed(false);
                    }
                }
                setPressViewHotSpot(e, pressedView);
                mPressedView.setPressed(true);
                for (Iterator it = childClickViewIds.iterator(); it.hasNext(); ) {
                    View childView = pressedView.findViewById((Integer) it.next());
                    childView.setPressed(false);
                }
                int position = vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount();
                onItemClick(baseQuickAdapter, pressedView, position);
            } else {
                setPressViewHotSpot(e, pressedView);
                mPressedView.setPressed(true);
                for (Iterator it = childClickViewIds.iterator(); it.hasNext(); ) {
                    View childView = pressedView.findViewById((Integer) it.next());
                    childView.setPressed(false);
                }
                int position = vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount();
                onItemClick(baseQuickAdapter, pressedView, position);
            }
            resetPressedView(pressedView);
            return true;
        }

        private View getViewClicked(ViewGroup childViewGroup, MotionEvent e) {
            View viewClicked = null;
            for (int i = 0; i < childViewGroup.getChildCount(); i++) {
                View child = childViewGroup.getChildAt(i);
                if (inRangeOfView(child, e) && child.isEnabled()) {
                    if (child instanceof ViewGroup) {
                        if (((ViewGroup) child).getChildCount() == 0 && childClickViewIds.contains(child.getId())) {
                            viewClicked = child;
                        } else {
                            viewClicked = getViewClicked((ViewGroup) child, e);
                        }
                    } else if (childClickViewIds.contains(child.getId())) {
                        viewClicked = child;
                    }
                }
            }
            if (viewClicked == null && childClickViewIds.contains(childViewGroup.getId())) {
                viewClicked = childViewGroup;
            }
            return viewClicked;
        }

        private void resetPressedView(final View pressedView) {
            if (pressedView != null) {
                pressedView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pressedView != null) {
                            pressedView.setPressed(false);
                        }

                    }
                }, 100);
            }

            mIsPrepressed = false;
            mPressedView = null;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            boolean isChildLongClick = false;
            if (!mIsPrepressed || mPressedView == null) {
                return;
            }

            mPressedView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            BaseViewHolder vh = (BaseViewHolder) recyclerView.getChildViewHolder(mPressedView);
            if (isHeaderOrFooterPosition(vh.getLayoutPosition())) {
                return;
            }

            longClickViewIds = vh.getItemChildLongClickViewIds();
            if (longClickViewIds == null || longClickViewIds.size() <= 0) {
                return;
            }
            for (Iterator it = longClickViewIds.iterator(); it.hasNext(); ) {
                View childView = mPressedView.findViewById((Integer) it.next());
                if (inRangeOfView(childView, e) && childView.isEnabled()) {
                    setPressViewHotSpot(e, childView);
                    int position = vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount();
                    onItemChildLongClick(baseQuickAdapter, childView, position);
                    childView.setPressed(true);
                    mIsShowPress = true;
                    isChildLongClick = true;
                    break;
                }
            }
            if (isChildLongClick) {
                return;
            }

            int position = vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount();
            onItemLongClick(baseQuickAdapter, mPressedView, position);
            setPressViewHotSpot(e, mPressedView);
            mPressedView.setPressed(true);
            for (Iterator it = longClickViewIds.iterator(); it.hasNext(); ) {
                View childView = mPressedView.findViewById((Integer) it.next());
                childView.setPressed(false);
            }
            mIsShowPress = true;

        }

    }

}


