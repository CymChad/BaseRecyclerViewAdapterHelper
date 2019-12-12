package com.chad.baserecyclerviewadapterhelper.adapter.node.section.provider;

import android.view.View;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.node.section.ItemNode;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SecondNodeProvider extends BaseNodeProvider<BaseViewHolder> {

    @Override
    public int getItemViewType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_section_content;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable BaseNode data) {
        if (data == null) {
            return;
        }

        ItemNode entity = (ItemNode) data;
        helper.setImageResource(R.id.iv, entity.getImg());
        helper.setText(R.id.tv, entity.getName());
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
    }
}
