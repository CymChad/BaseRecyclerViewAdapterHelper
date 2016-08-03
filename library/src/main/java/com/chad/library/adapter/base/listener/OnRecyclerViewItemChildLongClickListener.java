package com.chad.library.adapter.base.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Created by AllenCoder on 2016/8/03.
 * <p>
 * A convenience class to extend when you only want to OnRecyclerViewItemChildLongClickListener for a subset
 * of all the SimpleRecyclerViewClickListener. This implements all methods in the
 * {@link SimpleRecyclerViewClickListener}
 **/
public abstract class OnRecyclerViewItemChildLongClickListener extends SimpleRecyclerViewClickListener {


    /**
     * @param recyclerView     the parent recycleView
     * @param baseQuickAdapter this helper need the BaseQuickAdapter
     */
    public OnRecyclerViewItemChildLongClickListener(RecyclerView recyclerView, BaseQuickAdapter baseQuickAdapter) {
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

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
        SimpleOnItemChildLongClick(adapter,view,position);
    }
    public abstract void SimpleOnItemChildLongClick(BaseQuickAdapter adapter, View view, int position);
}
