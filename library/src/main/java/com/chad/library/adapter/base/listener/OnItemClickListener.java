package com.chad.library.adapter.base.listener;

import android.view.View;

import com.chad.library.adapter.base.XQuickAdapter;

public interface OnItemClickListener {

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     *
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     */
    void onItemClick(XQuickAdapter adapter, View view, int position);

    /**
     * callback method to be invoked when an item in this view has been
     * click and held
     *
     * @param view     The view whihin the AbsListView that was clicked
     * @param position The position of the view int the adapter
     * @return true if the callback consumed the long click ,false otherwise
     */
    void onItemLongClick(XQuickAdapter adapter, View view, int position);

    void onItemChildClick(XQuickAdapter adapter, View view, int position);

    void onItemChildLongClick(XQuickAdapter adapter, View view, int position);

}


