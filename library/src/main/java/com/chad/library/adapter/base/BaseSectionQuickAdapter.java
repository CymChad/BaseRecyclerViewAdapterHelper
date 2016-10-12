package com.chad.library.adapter.base;

import android.content.Context;
import android.view.ViewGroup;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.chad.library.adapter.base.vh.BaseViewHolder;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public abstract class BaseSectionQuickAdapter<T extends SectionEntity> extends BaseQuickAdapter<T> {


    protected static final int SECTION_HEADER_VIEW = 0x10000555;

    public BaseSectionQuickAdapter(Context context, List<T> data) {
        super(context, data);
    }

    @Override
    protected int getDefItemViewType(int position) {
        return getItem(position).isHeader ? SECTION_HEADER_VIEW : 0;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SECTION_HEADER_VIEW) {
            return createBaseViewHolder(parent, getSectionHeadResId());
        } else {
            return super.onCreateDefViewHolder(parent, viewType);
        }
    }

    /**
     * @param holder A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder holder, T item) {
        switch (holder.getItemViewType()) {
            case SECTION_HEADER_VIEW:
                setFullSpan(holder);
                convertHead(holder, item);
                break;
            default:
                convertContent(holder, item);
                break;
        }
    }

    protected abstract void convertHead(BaseViewHolder helper, T item);

    protected abstract void convertContent(BaseViewHolder helper, T item);

    protected abstract int getSectionHeadResId();
}
