package com.chad.baserecyclerviewadapterhelper.entity;

import androidx.annotation.Nullable;
import com.chad.baserecyclerviewadapterhelper.activity.treenode.adapter.TreeNodeAdapter;
import com.chad.library.adapter.base.BaseNode;
import java.util.List;

/**
 * 加载更多节点
 */
public class MyNodeLoadingEntity extends BaseNode {

    @Override
    public int getNodeType() {
        return TreeNodeAdapter.TYPE_LOAD_MORE;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNodes() {
        return null;
    }
}
