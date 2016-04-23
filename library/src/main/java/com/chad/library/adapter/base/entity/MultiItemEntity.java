package com.chad.library.adapter.base.entity;

/**
 * 项目名称：base-adapter-helper-recyclerview-master
 * 类描述：
 * 创建人：Chad
 * 创建时间：16/4/22 上午11:04
 */
public abstract class MultiItemEntity {
    protected int itemType;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
