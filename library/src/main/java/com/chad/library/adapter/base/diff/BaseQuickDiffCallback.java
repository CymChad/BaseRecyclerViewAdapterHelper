package com.chad.library.adapter.base.diff;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Extend this method to quickly implement DiffUtil
 *
 * @author limuyang
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

    public void setOldList(@Nullable List<T> oldList) {
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
        T oldItem = oldList.get(oldItemPosition);
        T newItem = newList.get(newItemPosition);
        if (oldItem != null && newItem != null) {
            return areItemsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
        } else {
            return oldItem == null && newItem == null;
        }
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T oldItem = oldList.get(oldItemPosition);
        T newItem = newList.get(newItemPosition);
        if (oldItem != null && newItem != null) {
            return areContentsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
        } else if (oldItem == null && newItem == null) {
            return true;
        } else {
            throw new AssertionError();
        }
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        T oldItem = oldList.get(oldItemPosition);
        T newItem = newList.get(newItemPosition);
        if (oldItem != null && newItem != null) {
            return getChangePayload(oldList.get(oldItemPosition), newList.get(newItemPosition));
        } else {
            throw new AssertionError();
        }
    }

    /**
     * @param oldItem New data
     * @param newItem old Data
     * @return Return false if items are no same
     */
    protected abstract boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem);

    /**
     * @param oldItem New data
     * @param newItem old Data
     * @return Return false if item content are no same
     */
    protected abstract boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem);

    /**
     * Optional implementation
     *
     * @param oldItem New data
     * @param newItem old Data
     * @return Payload info
     */
    @Nullable
    protected Object getChangePayload(@NonNull T oldItem, @NonNull T newItem) {
        return null;
    }
}
