package com.chad.baserecyclerviewadapterhelper.adapter.node.tree;

import com.chad.baserecyclerviewadapterhelper.adapter.node.tree.provider.FirstProvider;
import com.chad.baserecyclerviewadapterhelper.adapter.node.tree.provider.SecondProvider;
import com.chad.baserecyclerviewadapterhelper.adapter.node.tree.provider.ThirdProvider;
import com.chad.baserecyclerviewadapterhelper.entity.node.tree.FirstNode;
import com.chad.baserecyclerviewadapterhelper.entity.node.tree.SecondNode;
import com.chad.baserecyclerviewadapterhelper.entity.node.tree.ThirdNode;
import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NodeTreeAdapter extends BaseNodeAdapter<BaseViewHolder> {

    public NodeTreeAdapter() {
        super();
        addNodeProvider(new FirstProvider());
        addNodeProvider(new SecondProvider());
        addNodeProvider(new ThirdProvider());
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> data, int position) {
        BaseNode node = data.get(position);
        if (node instanceof FirstNode) {
            return 1;
        } else if (node instanceof SecondNode) {
            return 2;
        } else if (node instanceof ThirdNode) {
            return 3;
        }
        return -1;
    }
}
