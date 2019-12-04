package com.chad.baserecyclerviewadapterhelper.adapter.provider;

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
 * @description: Text ItemProvider
 * @date 2018/3/30  11:39
 */

public class TextItemProvider extends BaseItemProvider<ProviderMultiEntity, BaseViewHolder> {
    @Override
    public int getItemViewType() {
        return ProviderMultiEntity.TEXT;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_text_view;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable ProviderMultiEntity data) {
        helper.setText(R.id.tv, "CymChad content : " + helper.getAdapterPosition());
    }

    @Override
    public void onClick(@NonNull BaseViewHolder helper, ProviderMultiEntity data, int position) {
        Tips.show("Click: " + position);
    }

    @Override
    public boolean onLongClick(@NotNull BaseViewHolder helper, ProviderMultiEntity data, int position) {
        Tips.show("Long Click: " + position);
        return true;
    }
}
