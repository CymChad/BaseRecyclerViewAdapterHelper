package com.chad.baserecyclerviewadapterhelper.adapter;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.HomeItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class HomeAdapter extends BaseQuickAdapter<HomeItem> {
    public HomeAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void startAnim(Animator anim, int index) {
        super.startAnim(anim, index);
        if (index < 5)
        anim.setStartDelay(index * 150);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeItem item) {
        helper.setText(R.id.info_text, item.getTitle());
        CardView cardView = helper.getView(R.id.card_view);
        cardView.setCardBackgroundColor(Color.parseColor(item.getColorStr()));
    }
}
