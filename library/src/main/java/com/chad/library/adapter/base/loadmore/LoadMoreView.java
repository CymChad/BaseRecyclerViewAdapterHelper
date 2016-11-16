package com.chad.library.adapter.base.loadmore;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by BlingBling on 2016/11/11.
 */

public abstract class LoadMoreView {

    public static final int STATUS_DEFAULT = 1;
    public static final int STATUS_LOADING = 2;
    public static final int STATUS_FAIL = 3;
    public static final int STATUS_END = 4;

    private int mLoadMoreStatus = STATUS_DEFAULT;

    public void setLoadMoreStatus(int loadMoreStatus) {
        this.mLoadMoreStatus = loadMoreStatus;
    }

    public int getLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    public void convert(BaseViewHolder holder) {
        switch (mLoadMoreStatus) {
            case STATUS_LOADING:
                visibleLoading(holder, true);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, false);
                break;
            case STATUS_FAIL:
                visibleLoading(holder, false);
                visibleLoadFail(holder, true);
                visibleLoadEnd(holder, false);
                break;
            case STATUS_END:
                visibleLoading(holder, false);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, true);
                break;
        }
    }

    private void visibleLoading(BaseViewHolder holder, boolean visible) {
        holder.setVisible(getLoadingViewId(), visible);
    }

    private void visibleLoadFail(BaseViewHolder holder, boolean visible) {
        holder.setVisible(getLoadFailViewId(), visible);
    }

    private void visibleLoadEnd(BaseViewHolder holder, boolean visible) {
        if (isLoadEndGone()) {
            return;
        }
        holder.setVisible(getLoadEndViewId(), visible);
    }

    /**
     * load more layout
     *
     * @return
     */
    public abstract @LayoutRes int getLayoutId();

    /**
     * No more data is hidden
     *
     * @return true for no more data hidden load more
     */
    public abstract boolean isLoadEndGone();

    /**
     * loading view
     *
     * @return
     */
    protected abstract @IdRes int getLoadingViewId();

    /**
     * load fail view
     *
     * @return
     */
    protected abstract @IdRes int getLoadFailViewId();

    /**
     * load end view, If {@link LoadMoreView#isLoadEndGone()} is true, you can return 0
     *
     * @return
     */
    protected abstract @IdRes int getLoadEndViewId();
}
