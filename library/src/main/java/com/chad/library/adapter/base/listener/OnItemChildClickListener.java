package com.chad.library.adapter.base.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Created by AllenCoder on 2016/8/03.
 * A convenience class to extend when you only want to OnItemChildClickListener for a subset
 * of all the SimpleClickListener. This implements all methods in the
 * {@link SimpleClickListener}
 **/

public abstract class OnItemChildClickListener extends SimpleClickListener {
    public OnItemChildClickListener(RecyclerView recyclerView, BaseQuickAdapter baseQuickAdapter) {
        super(recyclerView, baseQuickAdapter);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        SimpleOnItemChildClick(adapter, view, position);
    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    public  abstract void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int position);

}
