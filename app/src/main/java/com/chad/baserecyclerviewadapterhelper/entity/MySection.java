package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class MySection<T> extends SectionEntity {
    public boolean isMroe;
    public MySection(boolean isHeader, String header,boolean isMroe) {
        super(isHeader, header);
        this.isMroe = isMroe;
    }
    public MySection(T t) {
        super(t);
    }
}
