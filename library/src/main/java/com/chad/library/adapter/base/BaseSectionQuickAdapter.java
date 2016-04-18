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


    protected int sectionHeadResId;
    protected List<T> data;
    protected static final int SECTION_HEADER_VIEW = 4;

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
        this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.context = context;
        this.layoutResId = layoutResId;
        this.sectionHeadResId = sectionHeadResId;
    }

    @Override
    public int getDefItemViewType(int position) {
        return data.get(position).isHeader ? SECTION_HEADER_VIEW : 0;
    }

    @Override
    public BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        View item = null;
        if (viewType == SECTION_HEADER_VIEW) {
            item = LayoutInflater.from(parent.getContext()).inflate(
                    sectionHeadResId, parent, false);
            return new SectionHeadViewHolder(item);
        } else {
            item = LayoutInflater.from(parent.getContext()).inflate(
                    layoutResId, parent, false);
            return new BaseViewHolder(context, item);
        }

    }

    public class SectionHeadViewHolder extends BaseViewHolder {

        public SectionHeadViewHolder(View itemView) {
            super(itemView.getContext(), itemView);
        }
    }


    @Override
    public void onBindDefViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
        int type = getItemViewType(position);
        if (type == SECTION_HEADER_VIEW) {
            int index = position - getHeaderViewsCount();
            convertHead(baseViewHolder, data.get(index));
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
