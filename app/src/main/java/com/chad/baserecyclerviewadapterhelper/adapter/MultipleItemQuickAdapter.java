package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.MultipleItem;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.vh.BaseViewHolder;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem> {

    public MultipleItemQuickAdapter(Context context, List data) {
        super(context, data);
        addItemType(MultipleItem.TEXT, R.layout.item_text_view);
        addItemType(MultipleItem.IMG, R.layout.item_image_view);
    }


    @Override protected int getGridLayoutManagerSpanSize(GridLayoutManager gridLayoutManager, int position) {
        return getItem(position).getSpanSize();
    }

    @Override protected void onCreateListener(BaseViewHolder holder) {

    }

    @Override protected void onBindViewHolder(BaseViewHolder holder, MultipleItem item) {
        switch (holder.getItemViewType()) {
            case MultipleItem.TEXT:
                holder.setText(R.id.tv, item.getContent());
                break;
            case MultipleItem.IMG:
                // set img data
                break;
        }
    }
}
