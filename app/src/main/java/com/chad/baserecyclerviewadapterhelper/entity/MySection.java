package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * 项目名称：BaseRecyclerViewAdapterHelper
 * 类描述：
 * 创建人：Chad
 * 创建时间：16/4/18 上午11:02
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
