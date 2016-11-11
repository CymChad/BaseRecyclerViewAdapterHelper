package com.chad.library.adapter.base;

import android.support.annotation.LayoutRes;

/**
 * Created by BlingBling on 2016/11/11.
 */

public abstract class LoadMoreView {
    public enum LoadMoreStatus {
        STATUS_DEFAULT,
        STATUS_LOADING,
        STATUS_LOAD_ERROR,
        STATUS_LOAD_END
    }

    private LoadMoreStatus mStatus = LoadMoreStatus.STATUS_DEFAULT;

    public void setLoadMoreStatus(LoadMoreStatus status){
        this.mStatus =status;
    }

    public LoadMoreStatus getLoadMoreStatus(){
        return mStatus;
    }

    public void convert(BaseViewHolder holder){
        switch (mStatus) {
            case STATUS_LOADING:
                onLoading(holder);
                break;
            case STATUS_LOAD_ERROR:
                onLoadError(holder);
                break;
            case STATUS_LOAD_END:
                onLoadEnd(holder);
                break;
        }
    }

    public abstract @LayoutRes int getLayoutId();

    public abstract boolean isLoadEndGone();

    protected abstract void onLoading(BaseViewHolder holder);

    protected abstract void onLoadError(BaseViewHolder holder);

    protected abstract void onLoadEnd(BaseViewHolder holder);
}
