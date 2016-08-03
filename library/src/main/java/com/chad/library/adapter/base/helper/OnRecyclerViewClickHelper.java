package com.chad.library.adapter.base.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * 作者: allen on 16/8/2.
 */

public abstract class OnRecyclerViewClickHelper extends OnRecyclerItemClickListener{
    public OnRecyclerViewClickHelper(RecyclerView recyclerView, BaseQuickAdapter baseQuickAdapter) {
        super(recyclerView, baseQuickAdapter);
    }

    @Override
    public void onItemClickHelper(View view, int position) {
        onItemClick( view, position);
    }

    @Override
    public void onItemLongClickHelper(View view, int position) {

    }

    @Override
    public void onItemChildClickHelper(View view, int position) {

    }
    public abstract void onItemClick(View view, int position);
}
