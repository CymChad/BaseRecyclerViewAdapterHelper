package com.chad.baserecyclerviewadapterhelper.entity;

import androidx.annotation.Nullable;

import com.chad.baserecyclerviewadapterhelper.activity.treenode.adapter.TreeNodeAdapter;
import com.chad.library.adapter.base.BaseNode;

import java.util.List;

/**
 * @author Dboy233
 */
public class FolderNodeEntity extends BaseNode {

    private String name;

    private List<BaseNode> childs;

    public FolderNodeEntity(String name, List<BaseNode> childs) {
        this.name = name;
        this.childs = childs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getNodeType() {
        return TreeNodeAdapter.TYPE_FOLDER;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNodes() {
        return childs;
    }
}
