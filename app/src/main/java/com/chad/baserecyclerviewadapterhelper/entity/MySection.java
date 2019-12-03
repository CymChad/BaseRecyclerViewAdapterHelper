package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.library.adapter.base.entity.JSectionEntity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class MySection<T> extends JSectionEntity {
    private boolean isHeader;
    private T t;

    public MySection(boolean isHeader, String header, T t) {
        this.isHeader = isHeader;
        this.t = t;
    }


    @Override
    public boolean isHeader() {
        return isHeader;
    }

}
