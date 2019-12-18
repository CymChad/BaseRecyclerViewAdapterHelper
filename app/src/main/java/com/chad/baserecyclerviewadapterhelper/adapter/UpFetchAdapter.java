package com.chad.baserecyclerviewadapterhelper.adapter;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.Movie;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chad.library.adapter.base.module.UpFetchModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author: limuyang
 * @date: 2019-12-06
 * @Description:
 */
public class UpFetchAdapter extends BaseQuickAdapter<Movie, BaseViewHolder> implements UpFetchModule {

    public UpFetchAdapter() {
        super(R.layout.item_header_and_footer);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, @Nullable Movie item) {
        switch (helper.getLayoutPosition() %
                3) {
            case 0:
                helper.setImageResource(R.id.iv, R.mipmap.animation_img1);
                break;
            case 1:
                helper.setImageResource(R.id.iv, R.mipmap.animation_img2);
                break;
            case 2:
                helper.setImageResource(R.id.iv, R.mipmap.animation_img3);
                break;
            default:
                break;
        }
    }
}
