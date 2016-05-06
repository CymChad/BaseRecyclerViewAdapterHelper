package com.chad.library.adapter.base.entity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
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
