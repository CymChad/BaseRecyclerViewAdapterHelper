package com.chad.library.adapter.base;

import java.util.List;

/**
 * Created by luoxw on 2016/8/8.
 */
public interface IExpandable<T> {
    boolean isExpanded();
    void setExpanded(boolean expanded);
    List<T> getSubItems();
}
