package com.chad.library.adapter.base;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.animation.AlphaInAnimation;
import com.chad.library.adapter.base.animation.BaseAnimation;
import com.chad.library.adapter.base.vh.BaseViewHolder;
import com.chad.library.adapter.base.vh.LoadMoreViewHolder;
import com.chad.library.adapter.base.vh.SimpleLoadMoreViewHolder;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by BlingBling on 2016/10/12.
 */

abstract class XQuickAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_HEADER_VIEW = 0x10000111;
    public static final int VIEW_TYPE_EMPTY_VIEW = 0x10000222;
    public static final int VIEW_TYPE_FOOTER_VIEW = 0x10000333;
    public static final int VIEW_TYPE_LOADING_VIEW = 0x10000444;

    protected List<T> mData;
    protected Context mContext;
    //header and footer
    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;
    //empty view
    private View mEmptyView;
    //load more
    private boolean mEnableLoadMore = false;
    private LoadMoreViewHolder mLoadMoreViewHolder = new SimpleLoadMoreViewHolder();
    private BaseQuickAdapter.RequestLoadMoreListener mRequestLoadMoreListener;
    //animation
    private int mLastPosition = -1;
    private boolean mFirstOnlyEnable = true;
    private BaseAnimation mSelectAnimation;

    public XQuickAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.mData = data == null ? new ArrayList<T>() : data;
    }

    public void setOnLoadMoreListener(BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener) {
        this.mRequestLoadMoreListener = requestLoadMoreListener;
    }

    /**
     * setting up a new instance to data;
     *
     * @param data
     */
    public void setNewData(List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        mLastPosition = -1;
        notifyDataSetChanged();
    }

    /**
     * insert a data associated with the specified position of adapter
     *
     * @param position
     * @param data
     */
    public void addData(int position, T data) {
        mData.add(position, data);
        notifyItemInserted(position + getHeaderViewCount());
    }

    /**
     * add one new data
     */
    public void addData(T data) {
        mData.add(data);
        notifyItemInserted(mData.size() - 1 + getHeaderViewCount());
    }

    /**
     * insert data list associated with the specified position of adapter
     *
     * @param position
     * @param data
     */
    public void addData(int position, List<T> data) {
        mData.addAll(position, data);
        notifyItemRangeInserted(position + getHeaderViewCount(), data.size());
    }

    /**
     * additional data;
     *
     * @param newData
     */
    public void addData(List<T> newData) {
        this.mData.addAll(newData);
        notifyItemRangeInserted(mData.size() - newData.size() + getHeaderViewCount(), newData.size());
    }

    /**
     * remove the item associated with the specified position of adapter
     *
     * @param position
     */
    public void removeData(int position) {
        mData.remove(position);
        notifyItemRemoved(position + getHeaderViewCount());
    }

    /**
     * remove data
     *
     * @param data
     * @return
     */
    public boolean removeData(T data) {
        if (data == null) {
            for (int i = 0, size = mData.size(); i < size; i++) {
                if (mData.get(i) == null) {
                    mData.remove(i);
                    notifyItemRemoved(i + getHeaderViewCount());
                    return true;
                }
            }
        } else {
            for (int i = 0, size = mData.size(); i < size; i++) {
                if (data.equals(mData.get(i))) {
                    mData.remove(i);
                    notifyItemRemoved(i + getHeaderViewCount());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get the data of list
     *
     * @return
     */
    public List<T> getData() {
        return mData;
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    /**
     * if setHeadView will be return 1 if not will be return 0.
     *
     * @return
     */
    public int getHeaderViewCount() {
        return mHeaderLayout == null ? 0 : 1;
    }

    /**
     * if mFooterLayout will be return 1 or not will be return 0.
     *
     * @return
     */
    public int getFooterViewCount() {
        return mFooterLayout == null ? 0 : 1;
    }

    public int getDataViewCount() {
        return mData.size();
    }

    public int getLoadMoreViewCount() {
        return mEnableLoadMore ? 1 : 0;
    }

    public boolean isEnableEmptyView() {
        if (getDataViewCount() == 0) {
            return mEmptyView != null;
        } else {
            return false;
        }
    }

    /**
     * Return root layout of header
     */
    public LinearLayout getHeaderLayout() {
        return mHeaderLayout;
    }

    /**
     * Return root layout of footer
     */
    public LinearLayout getFooterLayout() {
        return mFooterLayout;
    }

    /**
     * set a loadingView
     *
     * @param loadMoreViewHolder
     */
    public void setLoadMoreViewHolder(LoadMoreViewHolder loadMoreViewHolder) {
        this.mLoadMoreViewHolder = loadMoreViewHolder;
    }

    public void enableLoadMore(boolean enableLoadMore) {
        this.mEnableLoadMore = enableLoadMore;//这句话会造成getItemCount改变
        if (enableLoadMore) {
            mLoadMoreViewHolder.setLoadMoreStatus(LoadMoreViewHolder.STATUS_LOAD_DEFAULT);
            notifyItemInserted(getItemCount() - 1);
        } else {
            notifyItemRemoved(getItemCount());
        }
    }

    public void loadMoreComplete() {
        mLoadMoreViewHolder.setLoadMoreStatus(LoadMoreViewHolder.STATUS_LOAD_DEFAULT);
        notifyItemChanged(getItemCount() - 1);
    }

    public void loadMoreError() {
        mLoadMoreViewHolder.setLoadMoreStatus(LoadMoreViewHolder.STATUS_LOAD_ERROR);
        notifyItemChanged(getItemCount() - 1);
    }

    public void loadMoreEnd() {
        if (mLoadMoreViewHolder.isLoadEndGone()) {
            mEnableLoadMore = false;
            notifyItemRemoved(getItemCount() - 1);
        } else {
            mLoadMoreViewHolder.setLoadMoreStatus(LoadMoreViewHolder.STATUS_LOAD_END);
            notifyItemChanged(getItemCount() - 1);
        }
    }

    /**
     * returns the number of item that will be created
     *
     * @return
     */
    @Override
    public int getItemCount() {
        int count;
        if (isEnableEmptyView()) {
            count = 1;
        } else {
            count = getHeaderViewCount() + getFooterViewCount() + getDataViewCount() + getLoadMoreViewCount();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (isEnableEmptyView()) {
            return VIEW_TYPE_EMPTY_VIEW;
        } else {
            int numHeaders = getHeaderViewCount();
            if (position < numHeaders) {
                return VIEW_TYPE_HEADER_VIEW;
            } else {
                int adjPosition = position - numHeaders;
                int adapterCount = getDataViewCount();
                if (adjPosition < adapterCount) {
                    return getDefItemViewType(position - getHeaderViewCount());
                } else {
                    adjPosition = adjPosition - adapterCount;
                    int numFooters = getFooterViewCount();
                    if (adjPosition < numFooters) {
                        return VIEW_TYPE_FOOTER_VIEW;
                    } else {
                        return VIEW_TYPE_LOADING_VIEW;
                    }
                }
            }
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder;
        switch (viewType) {
            case VIEW_TYPE_HEADER_VIEW:
                baseViewHolder = new BaseViewHolder(mHeaderLayout);
                break;
            case VIEW_TYPE_FOOTER_VIEW:
                baseViewHolder = new BaseViewHolder(mFooterLayout);
                break;
            case VIEW_TYPE_LOADING_VIEW:
                baseViewHolder = createBaseViewHolder(parent, mLoadMoreViewHolder.getLoadMoreViewLayoutId());
                baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Log.e("TAG", "onclick---------->" + v);
                        if (mLoadMoreViewHolder.getLoadMoreStatus() == LoadMoreViewHolder.STATUS_LOAD_ERROR) {
                            mLoadMoreViewHolder.setLoadMoreStatus(LoadMoreViewHolder.STATUS_LOAD_DEFAULT);
                            notifyItemChanged(getItemCount() - 1);
                        }
                    }
                });
                break;
            case VIEW_TYPE_EMPTY_VIEW:
                baseViewHolder = new BaseViewHolder(mEmptyView);
                break;
            default:
                baseViewHolder = onCreateDefViewHolder(parent, viewType);
        }
        return baseViewHolder;
    }

    /**
     * To bind different types of holder and solve different the bind events
     *
     * @param holder
     * @param positions
     * @see #getDefItemViewType(int)
     */
    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int positions) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case VIEW_TYPE_HEADER_VIEW:
                break;
            case VIEW_TYPE_FOOTER_VIEW:
                break;
            case VIEW_TYPE_EMPTY_VIEW:
                break;
            case VIEW_TYPE_LOADING_VIEW:
                boolean loadMore = false;
                if (mLoadMoreViewHolder.getLoadMoreStatus() == LoadMoreViewHolder.STATUS_LOAD_DEFAULT) {
                    mLoadMoreViewHolder.setLoadMoreStatus(LoadMoreViewHolder.STATUS_LOADING);
                    loadMore = true;
                }
                mLoadMoreViewHolder.onStatusChanged(holder);
                if (loadMore) {
                    if (mRequestLoadMoreListener != null) {
                        mRequestLoadMoreListener.onLoadMoreRequested();
                    }
                }
                break;
            default:
                convert(holder, getItem(positions - getHeaderViewCount()));
                break;
        }

    }


    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutResId, parent, false);
        return new BaseViewHolder(view);
    }


    /**
     * When set to true, the item will layout using all span area. That means, if orientation
     * is vertical, the view will have full width; if orientation is horizontal, the view will
     * have full height.
     * if the hold view use StaggeredGridLayoutManager they should using all span area
     *
     * @param holder True if this item should traverse all spans.
     */
    protected void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     * simple to solve item will layout using all
     * {@link #setFullSpan(RecyclerView.ViewHolder)}
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (type == VIEW_TYPE_EMPTY_VIEW || type == VIEW_TYPE_HEADER_VIEW || type == VIEW_TYPE_FOOTER_VIEW || type == VIEW_TYPE_LOADING_VIEW) {
            setFullSpan(holder);
        } else {
            addAnimation(holder);
        }
    }


    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    int spanCount;
                    switch (type) {
                        case VIEW_TYPE_EMPTY_VIEW:
                        case VIEW_TYPE_HEADER_VIEW:
                        case VIEW_TYPE_FOOTER_VIEW:
                        case VIEW_TYPE_LOADING_VIEW:
                            spanCount = gridManager.getSpanCount();
                            break;
                        default:
                            spanCount = getGridLayoutManagerSpanSize(gridManager, position - getHeaderViewCount());
                            break;
                    }
                    return spanCount;
                }
            });
        }
    }

    protected int getGridLayoutManagerSpanSize(GridLayoutManager gridLayoutManager, int position) {
        return 1;
    }

    /**
     * Append header to the rear of the mHeaderLayout.
     *
     * @param header
     */
    public void addHeaderView(View header) {
        addHeaderView(header, -1);
    }

    /**
     * Add header view to mHeaderLayout and set header view position in mHeaderLayout.
     * When index = -1 or index >= child count in mHeaderLayout,
     * the effect of this method is the same as that of {@link #addHeaderView(View)}.
     *
     * @param header
     * @param index  the position in mHeaderLayout of this header.
     *               When index = -1 or index >= child count in mHeaderLayout,
     *               the effect of this method is the same as that of {@link #addHeaderView(View)}.
     */
    public void addHeaderView(View header, int index) {
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(header.getContext());
            mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
            mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        index = index > mHeaderLayout.getChildCount() ? -1 : index;
        mHeaderLayout.addView(header, index);
        notifyDataSetChanged();
    }

    /**
     * Append footer to the rear of the mFooterLayout.
     *
     * @param footer
     */
    public void addFooterView(View footer) {
        addFooterView(footer, -1);
    }

    /**
     * Add footer view to mFooterLayout and set footer view position in mFooterLayout.
     * When index = -1 or index >= child count in mFooterLayout,
     * the effect of this method is the same as that of {@link #addFooterView(View)}.
     *
     * @param footer
     * @param index  the position in mFooterLayout of this footer.
     *               When index = -1 or index >= child count in mFooterLayout,
     *               the effect of this method is the same as that of {@link #addFooterView(View)}.
     */
    public void addFooterView(View footer, int index) {
        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayout(footer.getContext());
            mFooterLayout.setOrientation(LinearLayout.VERTICAL);
            mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        index = index > mFooterLayout.getChildCount() ? -1 : index;
        mFooterLayout.addView(footer, index);
        notifyDataSetChanged();
    }

    /**
     * remove header view from mHeaderLayout.
     * When the child count of mHeaderLayout is 0, mHeaderLayout will be set to null.
     *
     * @param header
     */
    public void removeHeaderView(View header) {
        if (mHeaderLayout == null) return;

        mHeaderLayout.removeView(header);
        if (mHeaderLayout.getChildCount() == 0) {
            mHeaderLayout = null;
        }
        notifyDataSetChanged();
    }

    /**
     * remove footer view from mFooterLayout,
     * When the child count of mFooterLayout is 0, mFooterLayout will be set to null.
     *
     * @param footer
     */
    public void removeFooterView(View footer) {
        if (mFooterLayout == null) return;

        mFooterLayout.removeView(footer);
        if (mFooterLayout.getChildCount() == 0) {
            mFooterLayout = null;
        }
        notifyDataSetChanged();
    }

    /**
     * remove all header view from mHeaderLayout and set null to mHeaderLayout
     */
    public void removeAllHeaderView() {
        if (mHeaderLayout == null) return;

        mHeaderLayout.removeAllViews();
        mHeaderLayout = null;
        notifyDataSetChanged();
    }

    /**
     * remove all footer view from mFooterLayout and set null to mFooterLayout
     */
    public void removeAllFooterView() {
        if (mFooterLayout == null) return;

        mFooterLayout.removeAllViews();
        mFooterLayout = null;
        notifyDataSetChanged();
    }

    /**
     * set emptyView show if adapter is empty and want to show headview and footview
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    /**
     * When the current adapter is empty, the BaseQuickAdapter can display a special view
     * called the empty view. The empty view is used to provide feedback to the user
     * that no data is available in this AdapterView.
     *
     * @return The view to show if the adapter is empty.
     */
    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (mSelectAnimation != null) {
            if (!mFirstOnlyEnable || holder.getLayoutPosition() > mLastPosition) {
                mSelectAnimation.startAnimator(holder.itemView);
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    public interface RequestLoadMoreListener {
        void onLoadMoreRequested();
    }

    public void openLoadAnimation() {
        openLoadAnimation(new AlphaInAnimation());
    }

    /**
     * Set ObjectAnimator
     *
     * @param animation ObjectAnimator
     */
    public void openLoadAnimation(BaseAnimation animation) {
        this.mSelectAnimation = animation;
    }

    /**
     * {@link #addAnimation(RecyclerView.ViewHolder)}
     *
     * @param firstOnly true just show anim when first loading false show anim when load the data every time
     */
    public void isFirstOnly(boolean firstOnly) {
        this.mFirstOnlyEnable = firstOnly;
    }

    protected abstract int getDefItemViewType(int position);

    protected abstract BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType);

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract void convert(BaseViewHolder helper, T item);

}