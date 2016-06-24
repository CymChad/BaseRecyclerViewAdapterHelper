package com.chad.library.adapter.base.listener;

import android.support.v7.widget.RecyclerView;

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
     * @param pos If the view is swiped, pos will be negative.
     */
    void clearView(RecyclerView.ViewHolder viewHolder, int pos);
    /**
     * Called when item is swiped, the view is going to be removed from the adapter.
     */
    void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos);


}
