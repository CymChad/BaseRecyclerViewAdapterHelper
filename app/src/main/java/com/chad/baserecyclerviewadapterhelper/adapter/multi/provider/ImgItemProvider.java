package com.chad.baserecyclerviewadapterhelper.adapter.multi.provider;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.ProviderMultiEntity;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;

import org.jetbrains.annotations.NotNull;


/**
 * https://github.com/chaychan
 *
 * @author ChayChan
 * @description: Img ItemProvider
 * @date 2018/3/30  11:39
 */

public class ImgItemProvider extends BaseItemProvider<ProviderMultiEntity, BaseViewHolder> {

    @Override
    public int getItemViewType() {
        return ProviderMultiEntity.IMG;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_image_view;
    }

    @Override
    public void convert(@NonNull BaseViewHolder helper, @Nullable ProviderMultiEntity data) {
        if (helper.getAdapterPosition() % 2 == 0) {
            helper.setImageResource(R.id.iv, R.mipmap.animation_img1);
        } else {
            helper.setImageResource(R.id.iv, R.mipmap.animation_img2);
        }
    }

    @Override
    public void onClick(@NonNull BaseViewHolder helper, @NotNull View view, ProviderMultiEntity data, int position) {
        Tips.show("Click: " + position);
    }

    @Override
    public boolean onLongClick(@NotNull BaseViewHolder helper, @NotNull View view, ProviderMultiEntity data, int position) {
        Tips.show("Long Click: " + position);
        return true;
    }
}
