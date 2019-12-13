package com.chad.baserecyclerviewadapterhelper.adapter.node.tree.provider;

import android.view.View;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.node.tree.FirstNode;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FirstProvider extends BaseNodeProvider<BaseViewHolder> {

    @Override
    public int getItemViewType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_node_first;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable BaseNode data) {
        FirstNode entity = (FirstNode) data;
        helper.setText(R.id.title, entity.getTitle());

        if (entity.isExpanded()) {
            helper.setImageResource(R.id.iv, R.mipmap.arrow_b);
        } else {
            helper.setImageResource(R.id.iv, R.mipmap.arrow_r);
        }
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        FirstNode node = (FirstNode) data;
        if (node.isExpanded()) {
            getAdapter().collapse(position);
        } else {
            getAdapter().expand(position, false);
        }
    }
}
