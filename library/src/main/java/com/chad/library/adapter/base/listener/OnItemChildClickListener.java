package com.chad.library.adapter.base.listener;


import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

/**
 * @author: limuyang
 * @date: 2019-12-03
 * @Description:
 */
public interface OnItemChildClickListener<T, VH extends BaseViewHolder> {
    /**
     * callback method to be invoked when an item child in this view has been click
     *
     * @param adapter  BaseQuickAdapter
     * @param view     The view whihin the ItemView that was clicked
     * @param position The position of the view int the adapter
     */
    void onItemChildClick(@NonNull BaseQuickAdapter<T, VH> adapter, @NonNull View view, int position);
}
