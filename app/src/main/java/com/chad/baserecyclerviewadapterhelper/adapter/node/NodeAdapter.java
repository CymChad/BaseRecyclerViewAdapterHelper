package com.chad.baserecyclerviewadapterhelper.adapter.node;

import com.chad.baserecyclerviewadapterhelper.adapter.node.provider.RootFooterNodeProvider;
import com.chad.baserecyclerviewadapterhelper.adapter.node.provider.RootNodeProvider;
import com.chad.baserecyclerviewadapterhelper.adapter.node.provider.SecondNodeProvider;
import com.chad.baserecyclerviewadapterhelper.entity.node.RootFooterNode;
import com.chad.baserecyclerviewadapterhelper.entity.node.RootNode;
import com.chad.baserecyclerviewadapterhelper.entity.node.SecondNode;
import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NodeAdapter extends BaseNodeAdapter<BaseViewHolder> {

    public NodeAdapter() {
        super();
//        addSectionHeaderProvider(new SectionHeaderProvider());
        addFullSpanNodeProvider(new RootNodeProvider());
        addNodeProvider(new SecondNodeProvider());
        addFooterNodeProvider(new RootFooterNodeProvider());
    }



    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> data, int position) {
        BaseNode node = data.get(position);
        if (node instanceof RootNode) {
            return 0;
        } else if (node instanceof SecondNode) {
            return 1;
        } else if (node instanceof RootFooterNode) {
            return 2;
        }
        return -1;
    }
}
