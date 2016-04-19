package com.chad.library.adapter.base;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.chad.library.R;
import com.chad.library.adapter.base.animation.AlphaInAnimation;
import com.chad.library.adapter.base.animation.BaseAnimation;
import com.chad.library.adapter.base.animation.ScaleInAnimation;
import com.chad.library.adapter.base.animation.SlideInBottomAnimation;
import com.chad.library.adapter.base.animation.SlideInLeftAnimation;
import com.chad.library.adapter.base.animation.SlideInRightAnimation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public abstract class BaseQuickAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean mNextLoadEnable;
    private boolean mLoadingMoreEnable;
    private boolean mFirstOnlyEnable = true;
    private boolean mOpenAnimationEnable = false;

    @IntDef({ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }

    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int ALPHAIN = 0x00000001;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SCALEIN = 0x00000002;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_BOTTOM = 0x00000003;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_LEFT = 0x00000004;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_RIGHT = 0x00000005;


    protected static final String TAG = BaseQuickAdapter.class.getSimpleName();

    protected Context mContext;

    protected int mLayoutResId;

    protected List<T> mData;

    private Interpolator mInterpolator = new LinearInterpolator();

    private int mDuration = 300;

    private int mLastPosition = -1;


    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    private RequestLoadMoreListener mRequestLoadMoreListener;

    @AnimationType
    private BaseAnimation mCustomAnimation;
    private BaseAnimation mSelectAnimation = new AlphaInAnimation();


    protected static final int HEADER_VIEW = 0x00000001;
    protected static final int LOADING_VIEW = 0x00000002;
    protected static final int FOOTER_VIEW = 0x00000003;

    private View mHeaderView;
    private View mFooterView;

    public void setOnLoadMoreListener(int pageSize, RequestLoadMoreListener requestLoadMoreListener) {
        if (getItemCount() < pageSize) {
            return;
        }
        mNextLoadEnable = true;
        this.mRequestLoadMoreListener = requestLoadMoreListener;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener {
        public void onItemClick(View view, int position);
    }


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public BaseQuickAdapter(Context context, int layoutResId, List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.mContext = context;
        this.mLayoutResId = layoutResId;
    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);

    }

    public void add(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    public List getData() {
        return mData;
    }

    public int getHeaderViewsCount() {
        return mHeaderView == null ? 0 : 1;
    }

    public int getFooterViewsCount() {
        return mFooterView == null ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        int i = mNextLoadEnable ? 1 : 0;
        return mData.size() + i + getHeaderViewsCount() + getFooterViewsCount();
    }


    @Override
    public int getItemViewType(int position) {
        if (position < getHeaderViewsCount()) {
            return HEADER_VIEW;
        } else if (position == mData.size() + getHeaderViewsCount()) {
            if (mNextLoadEnable)
                return LOADING_VIEW;
            else
                return FOOTER_VIEW;
        }
        return getDefItemViewType(position);
    }

    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = null;
        if (viewType == LOADING_VIEW) {
            item = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.def_loading, parent, false);
            return new FooterViewHolder(item);
        } else if (viewType == HEADER_VIEW) {
            return new HeadViewHolder(mHeaderView);
        } else if (viewType == FOOTER_VIEW) {
            return new FooterViewHolder(mFooterView);
        } else {
            return onCreateDefViewHolder(parent, viewType);
        }

    }

    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                mLayoutResId, parent, false);
        return new BaseViewHolder(mContext, item);
    }

    public class FooterViewHolder extends BaseViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView.getContext(), itemView);
        }
    }

    public class HeadViewHolder extends BaseViewHolder {

        public HeadViewHolder(View itemView) {
            super(itemView.getContext(), itemView);
        }
    }


    public void addHeaderView(View header) {
        if (header == null) {
            throw new RuntimeException("header is null");
        }
        this.mHeaderView = header;
        this.notifyDataSetChanged();
    }

    public void addFooterView(View footer) {
        mNextLoadEnable = false;
        if (footer == null) {
            throw new RuntimeException("footer is null");
        }
        this.mFooterView = footer;
        this.notifyDataSetChanged();
    }

    public void isNextLoad(boolean isNextLoad) {
        mNextLoadEnable = isNextLoad;
        mLoadingMoreEnable = false;
        notifyDataSetChanged();

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
        int type = getItemViewType(position);
        int index;
        if (type == 0) {
            index = position - getHeaderViewsCount();
            convert(baseViewHolder, mData.get(index));
            if (onRecyclerViewItemClickListener != null) {
                baseViewHolder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRecyclerViewItemClickListener.onItemClick(v, position - getHeaderViewsCount());
                    }
                });
            }
            if (mOpenAnimationEnable) {
                if (!mFirstOnlyEnable || position > mLastPosition) {
                    BaseAnimation animation = null;
                    if (mCustomAnimation != null) {
                        animation = mCustomAnimation;
                    } else {
                        animation = mSelectAnimation;
                    }
                    for (Animator anim : animation.getAnimators(holder.itemView)) {
                        anim.setDuration(mDuration).start();
                        anim.setInterpolator(mInterpolator);
                    }
                    mLastPosition = position;
                }
            }
        } else if (type == LOADING_VIEW) {
            if (mNextLoadEnable && !mLoadingMoreEnable && mRequestLoadMoreListener != null) {
                mLoadingMoreEnable = true;
                mRequestLoadMoreListener.onLoadMoreRequested();
                if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                    params.setFullSpan(true);
                }
            }

        } else {
            onBindDefViewHolder(holder, position);
        }
    }

    protected void onBindDefViewHolder(RecyclerView.ViewHolder holder, final int position) {

    }

    public interface RequestLoadMoreListener {

        void onLoadMoreRequested();
    }


    /**
     * Set the view animation type.
     *
     * @param animationType One of {@link #ALPHAIN}, {@link #SCALEIN}, {@link #SLIDEIN_BOTTOM}, {@link #SLIDEIN_LEFT}, {@link #SLIDEIN_RIGHT}.
     */
    public void openLoadAnimation(@AnimationType int animationType) {
        this.mOpenAnimationEnable = true;
        mCustomAnimation = null;
        switch (animationType) {
            case ALPHAIN:
                mSelectAnimation = new AlphaInAnimation();
                break;
            case SCALEIN:
                mSelectAnimation = new ScaleInAnimation();
                break;
            case SLIDEIN_BOTTOM:
                mSelectAnimation = new SlideInBottomAnimation();
                break;
            case SLIDEIN_LEFT:
                mSelectAnimation = new SlideInLeftAnimation();
                break;
            case SLIDEIN_RIGHT:
                mSelectAnimation = new SlideInRightAnimation();
                break;
        }
    }

    /**
     * Set Custom ObjectAnimator
     *
     * @param animation ObjectAnimator
     */
    public void openLoadAnimation(BaseAnimation animation) {
        this.mOpenAnimationEnable = true;
        this.mCustomAnimation = animation;
    }

    public void openLoadAnimation() {
        this.mOpenAnimationEnable = true;
    }


    public void isFirstOnly(boolean firstOnly) {
        this.mFirstOnlyEnable = firstOnly;
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract void convert(BaseViewHolder helper, T item);


    @Override
    public long getItemId(int position) {
        return position;
    }


}
