package com.chad.library.adapter.base.vh;

/**
 * Created by BlingBling on 2016/10/11.
 */

public abstract class LoadMoreView {

    public static final int STATUS_LOAD_DEFAULT = 1;//默认状态用于判断用
    public static final int STATUS_LOADING = 2;//正在加载更多数据
    public static final int STATUS_LOAD_ERROR = 3;//加载更多失败
    public static final int STATUS_LOAD_END = 4;//上拉加载，再没有数据了

    private int mLoadMoreStatus = STATUS_LOAD_DEFAULT;

    public int getLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    public void setLoadMoreStatus(int loadMoreStatus) {
        this.mLoadMoreStatus = loadMoreStatus;
    }

    public abstract int getLoadMoreViewLayoutId();

    /**
     * 没有更多数据的时候是否隐藏
     *
     * @return
     */
    public abstract boolean isLoadEndGone();


    public void onStatusChanged(BaseViewHolder holder){
        switch (mLoadMoreStatus){
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

    protected abstract void onLoading(BaseViewHolder holder);

    protected abstract void onLoadError(BaseViewHolder holder);

    protected abstract void onLoadEnd(BaseViewHolder holder);
}
