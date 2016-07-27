
package com.chad.library.adapter.base;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public abstract class BaseQuickAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean mNextLoadEnable = false;
    private boolean mLoadingMoreEnable = false;
    private boolean mFirstOnlyEnable = true;
    private boolean mOpenAnimationEnable = false;
    private boolean mEmptyEnable;
    private boolean mHeadAndEmptyEnable;
    private boolean mFootAndEmptyEnable;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mDuration = 300;
    private int mLastPosition = -1;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener;
    private RequestLoadMoreListener mRequestLoadMoreListener;
    @AnimationType
    private BaseAnimation mCustomAnimation;
    private BaseAnimation mSelectAnimation = new AlphaInAnimation();
    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;
    private LinearLayout mCopyHeaderLayout = null;
    private LinearLayout mCopyFooterLayout = null;
    private int pageSize = -1;
    private View mContentView;
    /**
     * View to show if there are no items to show.
     */
    private View mEmptyView;

    protected static final String TAG = BaseQuickAdapter.class.getSimpleName();
    protected Context mContext;
    protected int mLayoutResId;
    protected LayoutInflater mLayoutInflater;
    protected List<T> mData;
    public static final int HEADER_VIEW = 0x00000111;
    public static final int LOADING_VIEW = 0x00000222;
    public static final int FOOTER_VIEW = 0x00000333;
    public static final int EMPTY_VIEW = 0x00000555;
    private View mLoadingView;

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

    /**
     * call the method will not enable the loadMore funcation and the params pageSize is invalid
     * more infomation see{@link  public void openLoadMore(int pageSize, boolean enable),@link  public void setOnLoadMoreListener(RequestLoadMoreListener requestLoadMoreListener)} method
     *
     * @param pageSize
     * @param requestLoadMoreListener
     */
    @Deprecated
    public void setOnLoadMoreListener(int pageSize, RequestLoadMoreListener requestLoadMoreListener) {

        setOnLoadMoreListener(requestLoadMoreListener);
    }

    public void setOnLoadMoreListener(RequestLoadMoreListener requestLoadMoreListener) {
        this.mRequestLoadMoreListener = requestLoadMoreListener;
    }

    /**
     * Sets the duration of the animation.
     *
     * @param duration The length of the animation, in milliseconds.
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    /**
     * when adapter's data size than pageSize and enable is true,the loading more function is enable,or disable
     *
     * @param pageSize
     * @param enable
     */
    public void openLoadMore(int pageSize, boolean enable) {
        this.pageSize = pageSize;
        mNextLoadEnable = enable;

    }

    /**
     * call the method before you should call setPageSize() method to setting up the enablePagerSize value,whether it will  invalid
     * enable the loading more data function if enable's value is true,or disable
     *
     * @param enable
     */
    public void openLoadMore(boolean enable) {
        mNextLoadEnable = enable;

    }

    /**
     * setting up the size to decide the loading more data funcation whether enable
     * enable if the data size than pageSize,or diable
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * return the value of pageSize
     *
     * @return
     */
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     * Register a callback to be invoked when an item in this AdapterView has
     * been clicked.
     *
     * @param onRecyclerViewItemClickListener The callback that will be invoked.
     */
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * AdapterView has been clicked.
     */
    public interface OnRecyclerViewItemClickListener {
        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         *
         * @param view     The view within the AdapterView that was clicked (this
         *                 will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         */
        public void onItemClick(View view, int position);
    }

    /**
     * Register a callback to be invoked when an item in this AdapterView has
     * been clicked and held
     *
     * @param onRecyclerViewItemLongClickListener The callback that will run
     */
    public void setOnRecyclerViewItemLongClickListener(OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener) {
        this.onRecyclerViewItemLongClickListener = onRecyclerViewItemLongClickListener;
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * view has been clicked and held
     */
    public interface OnRecyclerViewItemLongClickListener {
        /**
         * callback method to be invoked when an item in this view has been
         * click and held
         *
         * @param view     The view whihin the AbsListView that was clicked
         * @param position The position of the view int the adapter
         * @return true if the callback consumed the long click ,false otherwise
         */
        public boolean onItemLongClick(View view, int position);
    }

    private OnRecyclerViewItemChildClickListener mChildClickListener;

    private OnRecyclerViewItemChildLongClickListener mChildLongClickListener;

    /**
     * Register a callback to be invoked when childView in this AdapterView has
     * been clicked and held
     * {@link OnRecyclerViewItemChildClickListener}
     *
     * @param childClickListener The callback that will run
     */
    public void setOnRecyclerViewItemChildClickListener(OnRecyclerViewItemChildClickListener childClickListener) {
        this.mChildClickListener = childClickListener;
    }

    public interface OnRecyclerViewItemChildClickListener {
        void onItemChildClick(BaseQuickAdapter adapter, View view, int position);
    }

    public class OnItemChildClickListener implements View.OnClickListener {
        public RecyclerView.ViewHolder mViewHolder;

        @Override
        public void onClick(View v) {
            if (mChildClickListener != null)
                mChildClickListener.onItemChildClick(BaseQuickAdapter.this, v, mViewHolder.getLayoutPosition() - getHeaderLayoutCount());
        }
    }

    /**
     * Register a callback to be invoked when childView in this AdapterView has
     * been longClicked and held
     * {@link OnRecyclerViewItemChildLongClickListener}
     *
     * @param childLongClickListener The callback that will run
     */
    public void setOnRecyclerViewItemChildLongClickListener(OnRecyclerViewItemChildLongClickListener childLongClickListener) {
        this.mChildLongClickListener = childLongClickListener;
    }

    /**
     * Interface for ItemChildLongClick
     */
    public interface OnRecyclerViewItemChildLongClickListener {
        boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position);
    }

    public class OnItemChildLongClickListener implements View.OnLongClickListener {
        public RecyclerView.ViewHolder mViewHolder;

        @Override
        public boolean onLongClick(View v) {
            if (mChildLongClickListener != null) {
                return mChildLongClickListener.onItemChildLongClick(BaseQuickAdapter.this, v, mViewHolder.getLayoutPosition() - getHeaderViewsCount());
            }
            return false;
        }
    }


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public BaseQuickAdapter(int layoutResId, List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
    }

    public BaseQuickAdapter(List<T> data) {
        this(0, data);
    }

    public BaseQuickAdapter(View contentView, List<T> data) {
        this(0, data);
        mContentView = contentView;
    }

    /**
     * remove the item associated with the specified position of adapter
     *
     * @param position
     */
    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position + getHeaderLayoutCount());

    }

    /**
     * insert  a item associated with the specified position of adapter
     *
     * @param position
     * @param item
     */
    public void add(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }


    /**
     * setting up a new instance to data;
     *
     * @param data
     */
    public void setNewData(List<T> data) {
        this.mData = data;
        if (mRequestLoadMoreListener != null) {
            mNextLoadEnable = true;
            // mFooterLayout = null;
        }
        mLastPosition = -1;
        notifyDataSetChanged();
    }

    /**
     * additional data;
     *
     * @param data
     */
    public void addData(List<T> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * set a loadingView
     *
     * @param loadingView
     */
    public void setLoadingView(View loadingView) {
        this.mLoadingView = loadingView;
    }

    /**
     * Get the data of list
     *
     * @return
     */
    public List<T> getData() {
        return mData;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    public T getItem(int position) {
        return mData.get(position);
    }

    /**
     * if setHeadView will be return 1 if not will be return 0.
     * notice: Deprecated! Use {@link ViewGroup#getChildCount()} of {@link #getHeaderLayout()} to replace.
     *
     * @return
     */
    @Deprecated
    public int getHeaderViewsCount() {
        return mHeaderLayout == null ? 0 : 1;
    }

    /**
     * if mFooterLayout will be return 1 or not will be return 0.
     * notice: Deprecated! Use {@link ViewGroup#getChildCount()} of {@link #getFooterLayout()} to replace.
     *
     * @return
     */
    @Deprecated
    public int getFooterViewsCount() {
        return mFooterLayout == null ? 0 : 1;
    }

    /**
     * if addHeaderView will be return 1, if not will be return 0
     */
    public int getHeaderLayoutCount() {
        return mHeaderLayout == null ? 0 : 1;
    }

    /**
     * if addFooterView will be return 1, if not will be return 0
     */
    public int getFooterLayoutCount() {
        return mFooterLayout == null ? 0 : 1;
    }

    /**
     * if mEmptyView will be return 1 or not will be return 0
     *
     * @return
     */
    public int getmEmptyViewCount() {
        return mEmptyView == null ? 0 : 1;
    }

    /**
     * returns the number of item that will be created
     *
     * @return
     */
    @Override
    public int getItemCount() {
        int i = isLoadMore() ? 1 : 0;
        int count = mData.size() + i + getHeaderLayoutCount() + getFooterLayoutCount();
        if (mData.size() == 0 && mEmptyView != null) {
            /**
             *  setEmptyView(false) and add emptyView
             */
            if (count == 0 && (!mHeadAndEmptyEnable || !mFootAndEmptyEnable)) {
                count += getmEmptyViewCount();
                /**
                 * {@link #setEmptyView(true, true, View)}
                 */
            } else if (mHeadAndEmptyEnable || mFootAndEmptyEnable) {
                count += getmEmptyViewCount();
            }

            if ((mHeadAndEmptyEnable && getHeaderLayoutCount() == 1 && count == 1) || count == 0) {
                mEmptyEnable = true;
                count += getmEmptyViewCount();
            }

        }
        return count;
    }

    /**
     * Get the type of View that will be created by {@link #getItemView(int, ViewGroup)} for the specified item.
     *
     * @param position The position of the item within the adapter's data set whose view type we
     *                 want.
     * @return An integer representing the type of View. Two views should share the same type if one
     * can be converted to the other in {@link #getItemView(int, ViewGroup)}. Note: Integers must be in the
     * range 0 to {@link #getItemCount()} - 1.
     */
    @Override
    public int getItemViewType(int position) {
        /**
         * if set headView and positon =0
         */
        if (mHeaderLayout != null && position == 0) {
            return HEADER_VIEW;
        }
        /**
         * if user has no data and add emptyView and position <2{(headview +emptyView)}
         */
        if (mData.size() == 0 && mEmptyEnable && mEmptyView != null && position <= 2) {
            /**
             * if set {@link #setEmptyView(boolean, boolean, View)}  position = 1
             */
            if ((mHeadAndEmptyEnable || mFootAndEmptyEnable) && position == 1) {
                /**
                 * if user want to show headview and footview and emptyView but not add headview
                 */
                if (mHeaderLayout == null && mEmptyView != null && mFooterLayout != null) {
                    return FOOTER_VIEW;
                    /**
                     * add headview
                     */
                } else if (mHeaderLayout != null && mEmptyView != null) {
                    return EMPTY_VIEW;
                }
            } else if (position == 0) {
                /**
                 * has no emptyView just add emptyview
                 */
                if (mHeaderLayout == null) {
                    return EMPTY_VIEW;
                } else if (mFooterLayout != null)

                    return EMPTY_VIEW;


            } else if (position == 2 && (mFootAndEmptyEnable || mHeadAndEmptyEnable) && mHeaderLayout != null && mEmptyView != null) {
                return FOOTER_VIEW;

            } /**
             * user forget to set {@link #setEmptyView(boolean, boolean, View)}  but add footview and headview and emptyview
             */
            else if ((!mFootAndEmptyEnable || !mHeadAndEmptyEnable) && position == 1 && mFooterLayout != null) {
                return FOOTER_VIEW;
            }
        } else if (mData.size() == 0 && mEmptyView != null && getItemCount() == (mHeadAndEmptyEnable ? 2 : 1) && mEmptyEnable) {
            return EMPTY_VIEW;
        } else if (position == mData.size() + getHeaderLayoutCount()) {
            if (mNextLoadEnable)
                return LOADING_VIEW;
            else
                return FOOTER_VIEW;
        } else if (position > mData.size() + getHeaderLayoutCount()) {
            return FOOTER_VIEW;
        }
        return getDefItemViewType(position - getHeaderLayoutCount());
    }

    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = null;
        this.mContext = parent.getContext();
        this.mLayoutInflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case LOADING_VIEW:
                baseViewHolder = getLoadingView(parent);
                break;
            case HEADER_VIEW:
                baseViewHolder = new BaseViewHolder(mHeaderLayout);
                break;
            case EMPTY_VIEW:
                baseViewHolder = new BaseViewHolder(mEmptyView);
                break;
            case FOOTER_VIEW:
                baseViewHolder = new BaseViewHolder(mFooterLayout);
                break;
            default:
                baseViewHolder = onCreateDefViewHolder(parent, viewType);
                initItemClickListener(baseViewHolder);
        }
        return baseViewHolder;

    }


    private BaseViewHolder getLoadingView(ViewGroup parent) {
        if (mLoadingView == null) {
            return createBaseViewHolder(parent, R.layout.def_loading);
        }
        return new BaseViewHolder(mLoadingView);
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     * simple to solve item will layout using all
     * {@link #setFullSpan(RecyclerView.ViewHolder)}
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW) {
            setFullSpan(holder);
        } else {
            addAnimation(holder);
        }
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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    return (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * To bind different types of holder and solve different the bind events
     *
     * @param holder
     * @param positions
     * @see #getDefItemViewType(int)
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int positions) {
        int viewType = holder.getItemViewType();

        switch (viewType) {
            case 0:
                convert((BaseViewHolder) holder, mData.get(holder.getLayoutPosition() - getHeaderLayoutCount()));
                break;
            case LOADING_VIEW:
                addLoadMore(holder);
                break;
            case HEADER_VIEW:
                break;
            case EMPTY_VIEW:
                break;
            case FOOTER_VIEW:
                break;
            default:
                convert((BaseViewHolder) holder, mData.get(holder.getLayoutPosition() - getHeaderLayoutCount()));
                onBindDefViewHolder((BaseViewHolder) holder, mData.get(holder.getLayoutPosition() - getHeaderLayoutCount()));
                break;
        }

    }

    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, mLayoutResId);
    }

    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        if (mContentView == null) {
            return new BaseViewHolder(getItemView(layoutResId, parent));
        }
        return new BaseViewHolder(mContentView);
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
            if (mCopyHeaderLayout == null) {
                mHeaderLayout = new LinearLayout(header.getContext());
                mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
                mHeaderLayout.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                mCopyHeaderLayout = mHeaderLayout;
            } else {
                mHeaderLayout = mCopyHeaderLayout;
            }
        }
        index = index >= mHeaderLayout.getChildCount() ? -1 : index;
        mHeaderLayout.addView(header, index);
        this.notifyDataSetChanged();
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
        mNextLoadEnable = false;
        if (mFooterLayout == null) {
            if (mCopyFooterLayout == null) {
                mFooterLayout = new LinearLayout(footer.getContext());
                mFooterLayout.setOrientation(LinearLayout.VERTICAL);
                mFooterLayout.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                mCopyFooterLayout = mFooterLayout;
            } else {
                mFooterLayout = mCopyFooterLayout;
            }
        }
        index = index >= mFooterLayout.getChildCount() ? -1 : index;
        mFooterLayout.addView(footer, index);
        this.notifyDataSetChanged();
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
        this.notifyDataSetChanged();
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
        this.notifyDataSetChanged();
    }

    /**
     * remove all header view from mHeaderLayout and set null to mHeaderLayout
     */
    public void removeAllHeaderView() {
        if (mHeaderLayout == null) return;

        mHeaderLayout.removeAllViews();
        mHeaderLayout = null;
    }

    /**
     * remove all footer view from mFooterLayout and set null to mFooterLayout
     */
    public void removeAllFooterView() {
        if (mFooterLayout == null) return;

        mFooterLayout.removeAllViews();
        mFooterLayout = null;
    }

    /**
     * Sets the view to show if the adapter is empty
     */
    public void setEmptyView(View emptyView) {
        setEmptyView(false, false, emptyView);
    }

    /**
     * @param isHeadAndEmpty false will not show headView if the data is empty true will show emptyView and headView
     * @param emptyView
     */
    public void setEmptyView(boolean isHeadAndEmpty, View emptyView) {
        setEmptyView(isHeadAndEmpty, false, emptyView);
    }

    /**
     * set emptyView show if adapter is empty and want to show headview and footview
     *
     * @param isHeadAndEmpty
     * @param isFootAndEmpty
     * @param emptyView
     */
    public void setEmptyView(boolean isHeadAndEmpty, boolean isFootAndEmpty, View emptyView) {
        mHeadAndEmptyEnable = isHeadAndEmpty;
        mFootAndEmptyEnable = isFootAndEmpty;
        mEmptyView = emptyView;
        mEmptyEnable = true;
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
     * see more {@link  public void notifyDataChangedAfterLoadMore(boolean isNextLoad)}
     *
     * @param isNextLoad
     */
    @Deprecated
    public void isNextLoad(boolean isNextLoad) {
        mNextLoadEnable = isNextLoad;
        mLoadingMoreEnable = false;
        notifyDataSetChanged();

    }

    /**
     * @param isNextLoad true
     *                   if true when loading more data can show loadingView
     */
    public void notifyDataChangedAfterLoadMore(boolean isNextLoad) {
        mNextLoadEnable = isNextLoad;
        mLoadingMoreEnable = false;
        notifyDataSetChanged();

    }

    /**
     * add more data
     *
     * @param data
     * @param isNextLoad
     */
    public void notifyDataChangedAfterLoadMore(List<T> data, boolean isNextLoad) {
        mData.addAll(data);
        notifyDataChangedAfterLoadMore(isNextLoad);

    }


    private void addLoadMore(RecyclerView.ViewHolder holder) {
        if (isLoadMore() && !mLoadingMoreEnable) {
            mLoadingMoreEnable = true;
            mRequestLoadMoreListener.onLoadMoreRequested();
        }
    }

    /**
     * init the baseViewHolder to register onRecyclerViewItemClickListener and onRecyclerViewItemLongClickListener
     *
     * @param baseViewHolder
     */
    private void initItemClickListener(final BaseViewHolder baseViewHolder) {
        if (onRecyclerViewItemClickListener != null) {
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerViewItemClickListener.onItemClick(v, baseViewHolder.getLayoutPosition() - getHeaderLayoutCount());
                }
            });
        }
        if (onRecyclerViewItemLongClickListener != null) {
            baseViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onRecyclerViewItemLongClickListener.onItemLongClick(v, baseViewHolder.getLayoutPosition() - getHeaderLayoutCount());
                }
            });
        }
    }

    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (mOpenAnimationEnable) {
            if (!mFirstOnlyEnable || holder.getLayoutPosition() > mLastPosition) {
                BaseAnimation animation = null;
                if (mCustomAnimation != null) {
                    animation = mCustomAnimation;
                } else {
                    animation = mSelectAnimation;
                }
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    startAnim(anim, holder.getLayoutPosition());
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    /**
     * set anim to start when loading
     *
     * @param anim
     * @param index
     */
    protected void startAnim(Animator anim, int index) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
    }

    /**
     * Determine whether it is loaded more
     *
     * @return
     */
    private boolean isLoadMore() {
        return mNextLoadEnable && pageSize != -1 && mRequestLoadMoreListener != null && mData.size() >= pageSize;
    }

    /**
     * @param layoutResId ID for an XML layout resource to load
     * @param parent      Optional view to be the parent of the generated hierarchy or else simply an object that
     *                    provides a set of LayoutParams values for root of the returned
     *                    hierarchy
     * @return view will be return
     */
    protected View getItemView(int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, parent, false);
    }


    /**
     * @see #convert(BaseViewHolder, Object) ()
     * @deprecated This method is deprecated
     * {@link #convert(BaseViewHolder, Object)} depending on your use case.
     */
    @Deprecated
    protected void onBindDefViewHolder(BaseViewHolder holder, T item) {
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
            default:
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

    /**
     * To open the animation when loading
     */
    public void openLoadAnimation() {
        this.mOpenAnimationEnable = true;
    }

    /**
     * {@link #addAnimation(RecyclerView.ViewHolder)}
     *
     * @param firstOnly true just show anim when first loading false show anim when load the data every time
     */
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

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

}
