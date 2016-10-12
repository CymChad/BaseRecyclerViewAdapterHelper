package com.chad.library.adapter.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.vh.BaseViewHolder;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public abstract class BaseMultiItemQuickAdapter<T extends MultiItemEntity> extends BaseQuickAdapter<T> {

    private SparseArray<Integer> layouts = new SparseArray<>();

    public BaseMultiItemQuickAdapter(Context context, List<T> data) {
        super(context, data);
    }

    @Override protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected int getDefItemViewType(int position) {
        MultiItemEntity item = mData.get(position);
        return item.getItemType();
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, layouts.get(viewType));
    }

    protected void addItemType(int type, @LayoutRes int layoutResId) {
        layouts.put(type, layoutResId);
    }


}


