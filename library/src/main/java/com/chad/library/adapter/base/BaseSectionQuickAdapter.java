package com.chad.library.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public abstract class BaseSectionQuickAdapter<T extends SectionEntity> extends BaseQuickAdapter {


    protected int mSectionHeadResId;
    protected List<T> mData;
    protected static final int SECTION_HEADER_VIEW = 0x00000004;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param context          The context.
     * @param sectionHeadResId The section head layout id for each item
     * @param layoutResId      The layout resource id of each item.
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public BaseSectionQuickAdapter(Context context, int layoutResId, int sectionHeadResId, List<T> data) {
        super(context, layoutResId, data);
        this.mData = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.mContext = context;
        this.mLayoutResId = layoutResId;
        this.mSectionHeadResId = sectionHeadResId;
    }

    @Override
    protected int getDefItemViewType(int position) {
        return mData.get(position).isHeader ? SECTION_HEADER_VIEW : 0;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        View item = null;
        if (viewType == SECTION_HEADER_VIEW) {
            item = LayoutInflater.from(parent.getContext()).inflate(
                    mSectionHeadResId, parent, false);
            return new SectionHeadViewHolder(item);
        } else {
            item = LayoutInflater.from(parent.getContext()).inflate(
                    mLayoutResId, parent, false);
            return new BaseViewHolder(mContext, item);
        }

    }

    public class SectionHeadViewHolder extends BaseViewHolder {

        public SectionHeadViewHolder(View itemView) {
            super(itemView.getContext(), itemView);
        }
    }


    @Override
    protected void onBindDefViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
        int type = getItemViewType(position);
        if (type == SECTION_HEADER_VIEW) {
            int index = position - getHeaderViewsCount();
            convertHead(baseViewHolder, mData.get(index));
            if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                params.setFullSpan(true);
            }
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        convert(helper, (T) item);
    }

    protected abstract void convertHead(BaseViewHolder helper, T item);

    protected abstract void convert(BaseViewHolder helper, T item);


}
