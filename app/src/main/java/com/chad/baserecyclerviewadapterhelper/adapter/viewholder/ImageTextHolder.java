package com.chad.baserecyclerviewadapterhelper.adapter.viewholder;

import android.view.View;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.MultipleItem;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Wells on 2017/12/13.
 */

public class ImageTextHolder extends BaseViewHolder<MultipleItem> {

    public ImageTextHolder(View view) {
        super(view);
    }

    @Override
    public void onBind(MultipleItem data) {
        super.onBind(data);
        switch (getLayoutPosition() %
                2) {
            case 0:
                setImageResource(R.id.iv, R.mipmap.animation_img1);
                break;
            case 1:
                setImageResource(R.id.iv, R.mipmap.animation_img2);
                break;

        }
    }
}