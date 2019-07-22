package com.chad.library.adapter.base.diff;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.ListUpdateCallback;

import com.chad.library.adapter.base.BaseQuickAdapter;

public final class BaseQuickAdapterListUpdateCallback implements ListUpdateCallback {

    @NonNull
    private final BaseQuickAdapter mAdapter;

    public BaseQuickAdapterListUpdateCallback(@NonNull BaseQuickAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void onInserted(int position, int count) {
        this.mAdapter.notifyItemRangeInserted(position + mAdapter.getHeaderLayoutCount(), count);
    }

    public void onRemoved(int position, int count) {
        this.mAdapter.notifyItemRangeRemoved(position + mAdapter.getHeaderLayoutCount(), count);
    }

    public void onMoved(int fromPosition, int toPosition) {
        this.mAdapter.notifyItemMoved(fromPosition + mAdapter.getHeaderLayoutCount(), toPosition + mAdapter.getHeaderLayoutCount());
    }

    @Override
    public void onChanged(int position, int count, @Nullable Object payload) {
        this.mAdapter.notifyItemRangeChanged(position + mAdapter.getHeaderLayoutCount(), count, payload);
    }
}
