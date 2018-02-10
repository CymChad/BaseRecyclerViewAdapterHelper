package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.adapter.viewholder.ImageTextHolder;
import com.chad.baserecyclerviewadapterhelper.adapter.viewholder.TextHolder;
import com.chad.baserecyclerviewadapterhelper.entity.MultipleItem;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 * modify by AllenCoder
 */
public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder<MultipleItem>> {

    public MultipleItemQuickAdapter(Context context, List data) {
        super(data);
        addItemType(MultipleItem.TEXT, R.layout.item_text_view);
        addItemType(MultipleItem.IMG, R.layout.item_image_view);
        addItemType(MultipleItem.IMG_TEXT, R.layout.item_img_text_view);
    }

    @Override
    protected BaseViewHolder<MultipleItem> onCreateDefViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MultipleItem.TEXT:
                return new TextHolder(getItemView(R.layout.item_text_view, parent));
            case MultipleItem.IMG_TEXT:
                return new ImageTextHolder(getItemView(R.layout.item_img_text_view, parent));
            default:
                return super.onCreateDefViewHolder(parent, viewType);
        }
    }
}
