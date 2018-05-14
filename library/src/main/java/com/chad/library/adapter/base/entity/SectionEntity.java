package com.chad.library.adapter.base.entity;

import java.io.Serializable;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public abstract class SectionEntity<T> implements Serializable {
    public boolean isHeader;
    public T t;
    public String header;

    public SectionEntity(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
        this.t = null;
    }

    public SectionEntity(T t) {
        this.isHeader = false;
        this.header = null;
        this.t = t;
    }
}
