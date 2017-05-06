package com.chad.library.adapter.base.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>A helper to implement expandable item.</p>
 * <p>if you don't want to extent a class, you can also implement the interface IExpandable</p>
 * Created by luoxw on 2016/8/9.
 */
public abstract class AbstractExpandableItem<T> implements IExpandable<T> {
    private boolean mExpandable = false;
    private List<T> mSubItems;

    @Override
    public boolean isExpanded() {
        return mExpandable;
    }

    @Override
    public void setExpanded(boolean expanded) {
        mExpandable = expanded;
    }

    @Override
    public List<T> getSubItems() {
        return mSubItems;
    }

    private boolean hasSubItem() {
        return mSubItems != null && mSubItems.size() > 0;
    }

    public void setSubItems(List<T> list) {
        mSubItems = list;
    }

    public T getSubItem(int position) {
        if (hasSubItem() && position < mSubItems.size()) {
            return mSubItems.get(position);
        } else {
            return null;
        }
    }

    public int getSubItemPosition(T subItem) {
        return mSubItems != null ? mSubItems.indexOf(subItem) : -1;
    }

    public void addSubItem(T subItem) {
        if (mSubItems == null) {
            mSubItems = new ArrayList<>();
        }
        mSubItems.add(subItem);
    }

    public void addSubItem(int position, T subItem) {
        if (mSubItems != null && position >= 0 && position < mSubItems.size()) {
            mSubItems.add(position, subItem);
        } else {
            addSubItem(subItem);
        }
    }

    public boolean contains(T subItem) {
        return mSubItems != null && mSubItems.contains(subItem);
    }

    public boolean removeSubItem(T subItem) {
        return mSubItems != null && mSubItems.remove(subItem);
    }

    public boolean removeSubItem(int position) {
        if (mSubItems != null && position >= 0 && position < mSubItems.size()) {
            mSubItems.remove(position);
            return true;
        }
        return false;
    }
}
