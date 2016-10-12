package com.chad.library.adapter.base.vh;

/**
 * Created by BlingBling on 2016/10/11.
 */

public abstract class LoadMoreViewHolder {

    public static final int STATUS_LOAD_DEFAULT = 1;//上拉加载更多
    public static final int STATUS_LOADING = 2;//正在加载更多数据
    public static final int STATUS_LOAD_ERROR = 3;//加载更多失败
    public static final int STATUS_LOAD_END = 4;//上拉加载，再没有数据了

    private int mLoadMoreStatus = STATUS_LOAD_DEFAULT;
    private boolean mLoadingMore=false;

    public int getLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    public void setLoadMoreStatus(int loadMoreStatus) {
        this.mLoadMoreStatus = loadMoreStatus;
    }

    public boolean isLoadingMore() {
        return mLoadingMore;
    }

    public void setLoadingMore(boolean loadingMore) {
        this.mLoadingMore = loadingMore;
    }

    /**
     * 没有更多数据的时候是否隐藏
     *
     * @return
     */
    public boolean isLoadEndGone() {
        return false;
    }

    public abstract int getLoadMoreViewLayoutId();

    public abstract void onStatusChanged(BaseViewHolder holder);
}
