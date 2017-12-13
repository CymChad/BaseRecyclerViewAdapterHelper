package com.chad.baserecyclerviewadapterhelper.adapter.viewholder;

import android.view.View;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.MultipleItem;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Wells on 2017/12/13.
 */

public class TextHolder extends BaseViewHolder<MultipleItem> {

    public TextHolder(View view) {
        super(view);
    }

    @Override
    public void onBind(MultipleItem data) {
        super.onBind(data);
        setText(R.id.tv, data.getContent());
    }
}