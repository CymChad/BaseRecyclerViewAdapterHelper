package com.chad.library.adapter.base;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import com.chad.library.R;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;

import java.util.Collections;
import java.util.List;

/**
 * Created by luoxw on 2016/7/13.
 */
public abstract class BaseItemDraggableAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    private static final int NO_TOGGLE_VIEW = 0;
    protected int mToggleViewId = NO_TOGGLE_VIEW;
    protected ItemTouchHelper mItemTouchHelper;
    protected boolean itemDragEnabled = false;
    protected boolean itemSwipeEnabled = false;
    protected OnItemDragListener mOnItemDragListener;
    protected OnItemSwipeListener mOnItemSwipeListener;
    protected boolean mDragOnLongPress = true;

    protected View.OnTouchListener mOnToggleViewTouchListener;
    protected View.OnLongClickListener mOnToggleViewLongClickListener;

    private static final String ERROR_NOT_SAME_ITEMTOUCHHELPER = "Item drag and item swipe should pass the same ItemTouchHelper";


    public BaseItemDraggableAdapter(List<T> data) {
        super(data);
    }

    public BaseItemDraggableAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }


    /**
     * To bind different types of holder and solve different the bind events
     *
     * @param holder
     * @param positions
     * @see #getDefItemViewType(int)
     */
    @Override
    public void onBindViewHolder(K holder, int positions) {
        super.onBindViewHolder(holder, positions);
        int viewType = holder.getItemViewType();

        if (mItemTouchHelper != null && itemDragEnabled && viewType != LOADING_VIEW && viewType != HEADER_VIEW
                && viewType != EMPTY_VIEW && viewType != FOOTER_VIEW) {
            if (mToggleViewId != NO_TOGGLE_VIEW) {
                View toggleView = ((BaseViewHolder) holder).getView(mToggleViewId);
                if (toggleView != null) {
                    toggleView.setTag(R.id.BaseQuickAdapter_viewholder_support, holder);
                    if (mDragOnLongPress) {
                        toggleView.setOnLongClickListener(mOnToggleViewLongClickListener);
                    } else {
                        toggleView.setOnTouchListener(mOnToggleViewTouchListener);
                    }
                }
            } else {
                holder.itemView.setTag(R.id.BaseQuickAdapter_viewholder_support, holder);
                holder.itemView.setOnLongClickListener(mOnToggleViewLongClickListener);
            }
        }
    }


        /**
         * Set the toggle view's id which will trigger drag event.
         * If the toggle view id is not set, drag event will be triggered when the item is long pressed.
         *
         * @param toggleViewId the toggle view's id
         */
    public void setToggleViewId(int toggleViewId) {
        mToggleViewId = toggleViewId;
    }

    /**
     * Set the drag event should be trigger on long press.
     * Work when the toggleViewId has been set.
     *
     * @param longPress by default is true.
     */
    public void setToggleDragOnLongPress(boolean longPress) {
        mDragOnLongPress = longPress;
        if (mDragOnLongPress) {
            mOnToggleViewTouchListener = null;
            mOnToggleViewLongClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemTouchHelper != null && itemDragEnabled) {
                        mItemTouchHelper.startDrag((RecyclerView.ViewHolder) v.getTag(R.id.BaseQuickAdapter_viewholder_support));
                    }
                    return true;
                }
            };
        } else {
            mOnToggleViewTouchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN
                            && !mDragOnLongPress) {
                        if (mItemTouchHelper != null && itemDragEnabled) {
                            mItemTouchHelper.startDrag((RecyclerView.ViewHolder) v.getTag(R.id.BaseQuickAdapter_viewholder_support));
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            mOnToggleViewLongClickListener = null;
        }
    }

    /**
     * Enable drag items.
     * Use itemView as the toggleView when long pressed.
     *
     * @param itemTouchHelper {@link ItemTouchHelper}
     */
    public void enableDragItem(@NonNull ItemTouchHelper itemTouchHelper) {
        enableDragItem(itemTouchHelper, NO_TOGGLE_VIEW, true);
    }

    /**
     * Enable drag items. Use the specified view as toggle.
     *
     * @param itemTouchHelper {@link ItemTouchHelper}
     * @param toggleViewId    The toggle view's id.
     * @param dragOnLongPress If true the drag event will be trigger on long press, otherwise on touch down.
     */
    public void enableDragItem(@NonNull ItemTouchHelper itemTouchHelper, int toggleViewId, boolean dragOnLongPress) {
        itemDragEnabled = true;
        mItemTouchHelper = itemTouchHelper;
        setToggleViewId(toggleViewId);
        setToggleDragOnLongPress(dragOnLongPress);
    }

    /**
     * Disable drag items.
     */
    public void disableDragItem() {
        itemDragEnabled = false;
        mItemTouchHelper = null;
    }

    public boolean isItemDraggable() {
        return itemDragEnabled;
    }

    /**
     * <p>Enable swipe items.</p>
     * You should attach {@link ItemTouchHelper} which construct with {@link ItemDragAndSwipeCallback} to the Recycler when you enable this.
     */
    public void enableSwipeItem() {
        itemSwipeEnabled = true;
    }

    public void disableSwipeItem() {
        itemSwipeEnabled = false;
    }

    public boolean isItemSwipeEnable() {
        return itemSwipeEnabled;
    }

    /**
     * @param onItemDragListener Register a callback to be invoked when drag event happen.
     */
    public void setOnItemDragListener(OnItemDragListener onItemDragListener) {
        mOnItemDragListener = onItemDragListener;
    }

    public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition() - getHeaderLayoutCount();
    }

    public void onItemDragStart(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener.onItemDragStart(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void onItemDragMoving(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int from = getViewHolderPosition(source);
        int to = getViewHolderPosition(target);

        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(mData, i, i - 1);
            }
        }
        notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());

        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener.onItemDragMoving(source, from, target, to);
        }
    }

    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener.onItemDragEnd(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void setOnItemSwipeListener(OnItemSwipeListener listener) {
        mOnItemSwipeListener = listener;
    }

    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener.onItemSwipeStart(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void onItemSwipeClear(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener.clearView(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void onItemSwiped(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener.onItemSwiped(viewHolder, getViewHolderPosition(viewHolder));
        }

        int pos = getViewHolderPosition(viewHolder);

        mData.remove(pos);
        notifyItemRemoved(viewHolder.getAdapterPosition());
    }

    public void onItemSwiping(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener.onItemSwipeMoving(canvas, viewHolder, dX, dY, isCurrentlyActive);
        }
    }

}
