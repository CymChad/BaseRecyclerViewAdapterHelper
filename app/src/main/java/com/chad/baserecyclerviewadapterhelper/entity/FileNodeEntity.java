package com.chad.baserecyclerviewadapterhelper.entity;

import androidx.annotation.Nullable;

import com.chad.baserecyclerviewadapterhelper.activity.treenode.adapter.TreeNodeAdapter;
import com.chad.library.adapter.base.BaseNode;

import java.util.Date;
import java.util.List;

/**
 * @author Dboy233
 */
public class FileNodeEntity extends BaseNode {

    private String name;

    private Date time = new Date();

    public FileNodeEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public int getNodeType() {
        return TreeNodeAdapter.TYPE_FILE;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNodes() {
        return null;
    }
}
