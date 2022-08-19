package com.chad.library.adapter.base.diff;

import android.support.annotation.NonNull;

import java.util.List;

public interface ListChangeListener<T> {
    /**
     * Called after the current List has been updated.
     *
     * @param previousList The previous list.
     * @param currentList The new current list.
     */
    void onCurrentListChanged(@NonNull List<T> previousList, @NonNull List<T> currentList);
}
