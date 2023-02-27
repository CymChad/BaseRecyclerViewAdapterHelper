package com.chad.baserecyclerviewadapterhelper.entity.node.tree;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThirdNode extends BaseExpandNode {
    private String title;


    public ThirdNode(String title) {
        this.title = title;
    }





    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}
