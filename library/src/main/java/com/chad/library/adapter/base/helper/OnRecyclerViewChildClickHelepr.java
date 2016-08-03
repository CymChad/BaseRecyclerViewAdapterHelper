package com.chad.library.adapter.base.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Created by AllenCoder on 2016/8/03.
 *
 *
 * A convenience class to extend when you only want to OnRecyclerViewChildClickHelepr for a subset
 * of all the OnRecyclerItemClickListener. This implements all methods in the
 * {@link OnRecyclerItemClickListener}
 */
public abstract class OnRecyclerViewChildClickHelepr extends OnRecyclerItemClickListener{
    public OnRecyclerViewChildClickHelepr(RecyclerView recyclerView, BaseQuickAdapter baseQuickAdapter) {
        super(recyclerView, baseQuickAdapter);
    }

    @Override
    public void onItemClickHelper(View view, int position) {
        onItemChildClick(view,position);
    }

    @Override
    public void onItemLongClickHelper(View view, int position) {

    }

    @Override
    public void onItemChildClickHelper(View view, int position) {

    }
    public abstract  void onItemChildClick(View view, int position);
}
