package com.chad.library.adapter.base.listener;

import android.graphics.Canvas;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by luoxw on 2016/6/23.
 */
public interface OnItemSwipeListener {
    /**
     * Called when the swipe action start.
     */
    void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos);

    /**
     * Called when the swipe action is over.
     * If you change the view on the start, you should reset is here, no matter the item has swiped or not.
     *
     * @param pos If the view is swiped, pos will be negative.
     */
    void clearView(RecyclerView.ViewHolder viewHolder, int pos);

    /**
     * Called when item is swiped, the view is going to be removed from the adapter.
     */
    void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos);

    /**
     * Draw on the empty edge when swipe moving
     *
     * @param canvas            the empty edge's canvas
     * @param viewHolder        The ViewHolder which is being interacted by the User or it was
     *                          interacted and simply animating to its original position
     * @param dX                The amount of horizontal displacement caused by user's action
     * @param dY                The amount of vertical displacement caused by user's action
     * @param isCurrentlyActive True if this view is currently being controlled by the user or
     *                          false it is simply animating back to its original state.
     */
    void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive);
}
