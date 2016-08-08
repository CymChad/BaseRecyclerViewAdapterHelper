package com.chad.library.adapter.base.entity;

import java.io.Serializable;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public interface MultiItemEntity extends Serializable {

    public int getItemType();

    public void setItemType(int itemType);
}
