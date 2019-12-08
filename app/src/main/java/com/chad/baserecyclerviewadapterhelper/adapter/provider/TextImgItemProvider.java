package com.chad.baserecyclerviewadapterhelper.adapter.provider;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.ProviderMultiEntity;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * https://github.com/chaychan
 *
 * @author ChayChan
 * @description: Text Img ItemProvider
 * @date 2018/3/30  11:39
 */
public class TextImgItemProvider extends BaseItemProvider<ProviderMultiEntity, BaseViewHolder> {

    public TextImgItemProvider() {
        addChildClickViewIds(R.id.tv);
    }

    @Override
    public int getItemViewType() {
        return ProviderMultiEntity.IMG_TEXT;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_img_text_view;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable ProviderMultiEntity data) {
        helper.setText(R.id.tv, "CymChad " + helper.getAdapterPosition());
        if (helper.getAdapterPosition() % 2 == 0) {
            helper.setImageResource(R.id.iv, R.mipmap.animation_img1);
        } else {
            helper.setImageResource(R.id.iv, R.mipmap.animation_img2);
        }
    }

    /**
     * item 点击
     *
     * @param helper
     * @param data
     * @param position
     */
    @Override
    public void onClick(@NonNull BaseViewHolder helper, @NotNull View view, ProviderMultiEntity data, int position) {
        Tips.show("Click: " + position);
    }

    @Override
    public boolean onLongClick(@NotNull BaseViewHolder helper, @NotNull View view, ProviderMultiEntity data, int position) {
        Tips.show("Long Click: " + position);
        return true;
    }

    /**
     * 子控件点击
     *
     * @param helper
     * @param view
     * @param data
     * @param position
     */
    @Override
    public void onChildClick(@NotNull BaseViewHolder helper, @NotNull View view, ProviderMultiEntity data, int position) {
        if (view.getId() == R.id.tv) {
            Tips.show("TextView Click: " + position);
        }
    }
}
