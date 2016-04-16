/**
 * Copyright 2013 Joan Zapata
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chad.library.adapter.base;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
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


public abstract class BaseQuickAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private boolean mNextLoad;
    private boolean mIsLoadingMore;

    @IntDef({ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }

    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int ALPHAIN = 0;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SCALEIN = 1;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_BOTTOM = 2;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_LEFT = 3;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_RIGHT = 4;


    protected static final String TAG = BaseQuickAdapter.class.getSimpleName();

    protected final Context context;

    protected final int layoutResId;

    protected final List<T> data;

    private Interpolator mInterpolator = new LinearInterpolator();

    private int mDuration = 300;

    private int mLastPosition = -1;

    private boolean isFirstOnly = true;


    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    private RequestLoadMoreListener requestLoadMoreListener;

    @AnimationType
    int animationType = ALPHAIN;
    private BaseAnimation customAnimation = null;
    private BaseAnimation selectAnimation = new AlphaInAnimation();

    private boolean isOpenAnimation = false;

    public void setOnLoadMoreListener(int pageSize, RequestLoadMoreListener requestLoadMoreListener) {
        if (getItemCount() < pageSize) {
            return;
        }
        mNextLoad = true;
        this.requestLoadMoreListener = requestLoadMoreListener;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener {
        public void onItemClick(View view, int position);
    }


    /**
     * Create a QuickAdapter.
     *
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     */
    public BaseQuickAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
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
        this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.context = context;
        this.layoutResId = layoutResId;
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);

    }

    public void add(int position, T item) {
        data.add(position, item);
        notifyItemInserted(position);
    }

    public List getData() {
        return data;
    }

    public int getHeaderViewsCount() {
        return mHeaderViews.size();
    }

    public int getFooterViewsCount() {
        return mFooterViews.size();
    }

    @Override
    public int getItemCount() {
        int i = mNextLoad ? 1 : 0;
        return data.size() + i + getHeaderViewsCount() + getFooterViewsCount();
    }

    private static final int HEADER_VIEW = 2;
    private static final int LOADING_VIEW = 1;
    private static final int FOOTER_VIEW = 3;

    @Override
    public int getItemViewType(int position) {
        if (position < getHeaderViewsCount()) {
            return HEADER_VIEW;
        } else if (position == data.size() + getHeaderViewsCount()) {
            if (mNextLoad)
                return LOADING_VIEW;
            else
                return FOOTER_VIEW;
        }
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
            return new HeadViewHolder(mHeaderViews.get(0));
        } else if (viewType == FOOTER_VIEW) {
            return new HeadViewHolder(mFooterViews.get(0));
        } else {
            item = LayoutInflater.from(parent.getContext()).inflate(
                    layoutResId, parent, false);
            return new BaseViewHolder(context, item);
        }

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

    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFooterViews = new ArrayList<>();

    public void addHeaderView(View header) {
        if (header == null) {
            throw new RuntimeException("header is null");
        }
        if (mHeaderViews.size() == 0)
            mHeaderViews.add(header);
        this.notifyDataSetChanged();
    }

    public void addFooterView(View header) {
        mNextLoad = false;
        if (header == null) {
            throw new RuntimeException("header is null");
        }
        if (mFooterViews.size() == 0)
            mFooterViews.add(header);
        this.notifyDataSetChanged();
    }

    public void isNextLoad(boolean isNextLoad) {
        mNextLoad = isNextLoad;
        mIsLoadingMore = false;
        notifyDataSetChanged();

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
        int type = getItemViewType(position);
        int index;
        if (type == 0) {
            index = position - getHeaderViewsCount();
            convert(baseViewHolder, data.get(index));
            if (onRecyclerViewItemClickListener != null) {
                baseViewHolder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRecyclerViewItemClickListener.onItemClick(v, position - getHeaderViewsCount());
                    }
                });
            }
            if (isOpenAnimation) {
                if (!isFirstOnly || position > mLastPosition) {
                    BaseAnimation animation = null;
                    if (customAnimation != null) {
                        animation = customAnimation;
                    } else {
                        animation = selectAnimation;
                    }
                    for (Animator anim : animation.getAnimators(holder.itemView)) {
                        anim.setDuration(mDuration).start();
                        anim.setInterpolator(mInterpolator);
                    }
                    mLastPosition = position;
                }
            }
        } else if (type == LOADING_VIEW) {
            if (mNextLoad && !mIsLoadingMore && requestLoadMoreListener != null) {
                mIsLoadingMore = true;
                requestLoadMoreListener.onLoadMoreRequested();
            }

        }
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
        this.isOpenAnimation = true;
        customAnimation = null;
        switch (animationType) {
            case ALPHAIN:
                selectAnimation = new AlphaInAnimation();
                break;
            case SCALEIN:
                selectAnimation = new ScaleInAnimation();
                break;
            case SLIDEIN_BOTTOM:
                selectAnimation = new SlideInBottomAnimation();
                break;
            case SLIDEIN_LEFT:
                selectAnimation = new SlideInLeftAnimation();
                break;
            case SLIDEIN_RIGHT:
                selectAnimation = new SlideInRightAnimation();
                break;
        }
    }

    /**
     * Set Custom ObjectAnimator
     *
     * @param animation ObjectAnimator
     */
    public void openLoadAnimation(BaseAnimation animation) {
        this.isOpenAnimation = true;
        this.customAnimation = animation;
    }

    public void openLoadAnimation() {
        this.isOpenAnimation = true;
    }


    public void setFirstOnly(boolean firstOnly) {
        isFirstOnly = firstOnly;
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
