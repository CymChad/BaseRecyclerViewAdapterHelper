package com.afollestad.dragselectrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.chad.library.R;

/**
 * @author Aidan Follestad (afollestad)
 */
public class DragSelectRecyclerView extends RecyclerView {

    public interface FingerListener {
        void onDragSelectFingerAction(boolean fingerDown);
    }

    private static final boolean LOGGING = false;
    private static final int AUTO_SCROLL_DELAY = 25;

    public DragSelectRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public DragSelectRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DragSelectRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private static void LOG(String message, Object... args) {
        //noinspection PointlessBooleanExpression
        if (!LOGGING) return;
        if (args != null) {
            Log.d("DragSelectRecyclerView", String.format(message, args));
        } else {
            Log.d("DragSelectRecyclerView", message);
        }
    }

    private int mLastDraggedIndex = -1;
    private DragSelectRecyclerViewAdapter<?> mAdapter;
    private int mInitialSelection;
    private boolean mDragSelectActive;
    private int mMinReached;
    private int mMaxReached;

    private int mHotspotHeight;
    private int mHotspotOffsetTop;
    private int mHotspotOffsetBottom;

    private int mHotspotTopBoundStart;
    private int mHotspotTopBoundEnd;
    private int mHotspotBottomBoundStart;
    private int mHotspotBottomBoundEnd;
    private int mAutoScrollVelocity;

    private FingerListener mFingerListener;

    private void init(Context context, AttributeSet attrs) {
        mAutoScrollHandler = new Handler();
        final int defaultHotspotHeight = context.getResources().getDimensionPixelSize(R.dimen.dsrv_defaultHotspotHeight);

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DragSelectRecyclerView, 0, 0);
            try {
                boolean autoScrollEnabled = a.getBoolean(R.styleable.DragSelectRecyclerView_dsrv_autoScrollEnabled, true);
                if (!autoScrollEnabled) {
                    mHotspotHeight = -1;
                    mHotspotOffsetTop = -1;
                    mHotspotOffsetBottom = -1;
                    LOG("Auto-scroll disabled");
                } else {
                    mHotspotHeight = a.getDimensionPixelSize(
                            R.styleable.DragSelectRecyclerView_dsrv_autoScrollHotspotHeight, defaultHotspotHeight);
                    mHotspotOffsetTop = a.getDimensionPixelSize(
                            R.styleable.DragSelectRecyclerView_dsrv_autoScrollHotspot_offsetTop, 0);
                    mHotspotOffsetBottom = a.getDimensionPixelSize(
                            R.styleable.DragSelectRecyclerView_dsrv_autoScrollHotspot_offsetBottom, 0);
                    LOG("Hotspot height = %d", mHotspotHeight);
                }
            } finally {
                a.recycle();
            }
        } else {
            mHotspotHeight = defaultHotspotHeight;
            LOG("Hotspot height = %d", mHotspotHeight);
        }
    }

    public void setFingerListener(@Nullable FingerListener listener) {
        this.mFingerListener = listener;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (mHotspotHeight > -1) {
            mHotspotTopBoundStart = mHotspotOffsetTop;
            mHotspotTopBoundEnd = mHotspotOffsetTop + mHotspotHeight;
            mHotspotBottomBoundStart = (getMeasuredHeight() - mHotspotHeight) - mHotspotOffsetBottom;
            mHotspotBottomBoundEnd = getMeasuredHeight() - mHotspotOffsetBottom;
            LOG("RecyclerView height = %d", getMeasuredHeight());
            LOG("Hotspot top bound = %d to %d", mHotspotTopBoundStart, mHotspotTopBoundStart);
            LOG("Hotspot bottom bound = %d to %d", mHotspotBottomBoundStart, mHotspotBottomBoundEnd);
        }
    }

    public boolean setDragSelectActive(boolean active, int initialSelection) {
        if (active && mDragSelectActive) {
            LOG("Drag selection is already active.");
            return false;
        }
        mLastDraggedIndex = -1;
        mMinReached = -1;
        mMaxReached = -1;
        if (!mAdapter.isIndexSelectable(initialSelection)) {
            mDragSelectActive = false;
            mInitialSelection = -1;
            mLastDraggedIndex = -1;
            LOG("Index %d is not selectable.", initialSelection);
            return false;
        }
        mAdapter.setSelected(initialSelection, true);
        mDragSelectActive = active;
        mInitialSelection = initialSelection;
        mLastDraggedIndex = initialSelection;
        if (mFingerListener != null)
            mFingerListener.onDragSelectFingerAction(true);
        LOG("Drag selection initialized, starting at index %d.", initialSelection);
        return true;
    }

    /**
     * Use {@link #setAdapter(DragSelectRecyclerViewAdapter)} instead.
     */
    @Override
    @Deprecated
    public void setAdapter(Adapter adapter) {
        if (!(adapter instanceof DragSelectRecyclerViewAdapter<?>))
            throw new IllegalArgumentException("Adapter must be a DragSelectRecyclerViewAdapter.");
        setAdapter((DragSelectRecyclerViewAdapter<?>) adapter);
    }

    public void setAdapter(DragSelectRecyclerViewAdapter<?> adapter) {
        super.setAdapter(adapter);
        mAdapter = adapter;
    }

    private boolean mInTopHotspot;
    private boolean mInBottomHotspot;

    private Handler mAutoScrollHandler;
    private Runnable mAutoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (mAutoScrollHandler == null)
                return;
            if (mInTopHotspot) {
                scrollBy(0, -mAutoScrollVelocity);
                mAutoScrollHandler.postDelayed(this, AUTO_SCROLL_DELAY);
            } else if (mInBottomHotspot) {
                scrollBy(0, mAutoScrollVelocity);
                mAutoScrollHandler.postDelayed(this, AUTO_SCROLL_DELAY);
            }
        }
    };

    private int getItemPosition(MotionEvent e) {
        final View v = findChildViewUnder(e.getX(), e.getY());
        if (v == null) return NO_POSITION;
        Object tag = v.getTag();
        if (tag == null || !(tag instanceof ViewHolder))
            throw new IllegalStateException("Make sure your adapter makes a call to super.onBindViewHolder(), and doesn't override itemView tags.");
        final ViewHolder holder = (ViewHolder) v.getTag();
        return holder.getAdapterPosition();
    }

    private RectF mTopBoundRect;
    private RectF mBottomBoundRect;
    private Paint mDebugPaint;
    private boolean mDebugEnabled = false;

    public final void enableDebug() {
        mDebugEnabled = true;
        invalidate();
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);

        if (mDebugEnabled) {
            if (mDebugPaint == null) {
                mDebugPaint = new Paint();
                mDebugPaint.setColor(Color.BLACK);
                mDebugPaint.setAntiAlias(true);
                mDebugPaint.setStyle(Paint.Style.FILL);
                mTopBoundRect = new RectF(0, mHotspotTopBoundStart, getMeasuredWidth(), mHotspotTopBoundEnd);
                mBottomBoundRect = new RectF(0, mHotspotBottomBoundStart, getMeasuredWidth(), mHotspotBottomBoundEnd);
            }
            c.drawRect(mTopBoundRect, mDebugPaint);
            c.drawRect(mBottomBoundRect, mDebugPaint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (mAdapter==null || mAdapter.getItemCount() == 0)
            return super.dispatchTouchEvent(e);

        if (mDragSelectActive) {
            final int itemPosition = getItemPosition(e);
            if (e.getAction() == MotionEvent.ACTION_UP) {
                mDragSelectActive = false;
                mInTopHotspot = false;
                mInBottomHotspot = false;
                mAutoScrollHandler.removeCallbacks(mAutoScrollRunnable);
                if (mFingerListener != null)
                    mFingerListener.onDragSelectFingerAction(false);
                return true;
            } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                // Check for auto-scroll hotspot
                if (mHotspotHeight > -1) {
                    if (e.getY() >= mHotspotTopBoundStart && e.getY() <= mHotspotTopBoundEnd) {
                        mInBottomHotspot = false;
                        if (!mInTopHotspot) {
                            mInTopHotspot = true;
                            LOG("Now in TOP hotspot");
                            mAutoScrollHandler.removeCallbacks(mAutoScrollRunnable);
                            mAutoScrollHandler.postDelayed(mAutoScrollRunnable, AUTO_SCROLL_DELAY);
                        }

                        final float simulatedFactor = mHotspotTopBoundEnd - mHotspotTopBoundStart;
                        final float simulatedY = e.getY() - mHotspotTopBoundStart;
                        mAutoScrollVelocity = (int) (simulatedFactor - simulatedY) / 2;

                        LOG("Auto scroll velocity = %d", mAutoScrollVelocity);
                    } else if (e.getY() >= mHotspotBottomBoundStart && e.getY() <= mHotspotBottomBoundEnd) {
                        mInTopHotspot = false;
                        if (!mInBottomHotspot) {
                            mInBottomHotspot = true;
                            LOG("Now in BOTTOM hotspot");
                            mAutoScrollHandler.removeCallbacks(mAutoScrollRunnable);
                            mAutoScrollHandler.postDelayed(mAutoScrollRunnable, AUTO_SCROLL_DELAY);
                        }

                        final float simulatedY = e.getY() + mHotspotBottomBoundEnd;
                        final float simulatedFactor = mHotspotBottomBoundStart + mHotspotBottomBoundEnd;
                        mAutoScrollVelocity = (int) (simulatedY - simulatedFactor) / 2;

                        LOG("Auto scroll velocity = %d", mAutoScrollVelocity);
                    } else if (mInTopHotspot || mInBottomHotspot) {
                        LOG("Left the hotspot");
                        mAutoScrollHandler.removeCallbacks(mAutoScrollRunnable);
                        mInTopHotspot = false;
                        mInBottomHotspot = false;
                    }
                }

                // Drag selection logic
                if (itemPosition != NO_POSITION && mLastDraggedIndex != itemPosition) {
                    mLastDraggedIndex = itemPosition;
                    if (mMinReached == -1) mMinReached = mLastDraggedIndex;
                    if (mMaxReached == -1) mMaxReached = mLastDraggedIndex;
                    if (mLastDraggedIndex > mMaxReached)
                        mMaxReached = mLastDraggedIndex;
                    if (mLastDraggedIndex < mMinReached)
                        mMinReached = mLastDraggedIndex;
                    if (mAdapter != null)
                        mAdapter.selectRange(mInitialSelection, mLastDraggedIndex, mMinReached, mMaxReached);
                    if (mInitialSelection == mLastDraggedIndex) {
                        mMinReached = mLastDraggedIndex;
                        mMaxReached = mLastDraggedIndex;
                    }
                }
                return true;
            }
        }
        return super.dispatchTouchEvent(e);
    }
}