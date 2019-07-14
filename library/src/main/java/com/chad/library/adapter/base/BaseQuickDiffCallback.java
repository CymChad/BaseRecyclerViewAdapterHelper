package com.chad.library.adapter.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Extend this method to quickly implement DiffUtil
 * @param <T> Data type
 */
public abstract class BaseQuickDiffCallback<T> extends DiffUtil.Callback {

    private List<T> newList;
    private List<T> oldList;

    public BaseQuickDiffCallback(@Nullable List<T> newList) {
        this.newList = newList == null ? new ArrayList<T>() : newList;
    }

    public List<T> getNewList() {
        return newList;
    }

    public List<T> getOldList() {
        return oldList;
    }

    void setOldList(@Nullable List<T> oldList) {
        this.oldList = oldList == null ? new ArrayList<T>() : oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return areItemsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return areContentsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return getChangePayload(oldList.get(oldItemPosition), newList.get(newItemPosition));
    }

    /**
     * @param oldItem New data
     * @param newItem old Data
     * @return Return false if items are no same
     */
    protected abstract boolean areItemsTheSame(T oldItem, T newItem);

    /**
     * @param oldItem New data
     * @param newItem old Data
     * @return Return false if item content are no same
     */
    protected abstract boolean areContentsTheSame(T oldItem, T newItem);

    /**
     * Optional implementation
     *
     * @param oldItem New data
     * @param newItem old Data
     * @return Payload info
     */
    protected Object getChangePayload(T oldItem, T newItem) {
        return null;
    }
}
