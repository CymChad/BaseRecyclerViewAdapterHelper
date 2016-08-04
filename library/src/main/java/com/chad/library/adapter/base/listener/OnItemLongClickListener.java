package com.chad.library.adapter.base.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * create by: allen on 16/8/3.
 */

public abstract class OnItemLongClickListener extends SimpleClickListener {


    /**
     * @param recyclerView     the parent recycleView
     * @param baseQuickAdapter this helper need the BaseQuickAdapter
     */
    public OnItemLongClickListener(RecyclerView recyclerView, BaseQuickAdapter baseQuickAdapter) {
        super(recyclerView, baseQuickAdapter);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        SimpleOnItemLongClick( adapter,  view,  position);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
    }
    public abstract void SimpleOnItemLongClick(BaseQuickAdapter adapter, View view, int position);
}
