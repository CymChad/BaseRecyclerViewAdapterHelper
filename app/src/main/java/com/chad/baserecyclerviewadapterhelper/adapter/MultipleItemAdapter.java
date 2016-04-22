package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class MultipleItemAdapter extends BaseQuickAdapter<String> {
    private static final int TEXT_TYPE = 1;
    private int mTextLayoutResId;


    public MultipleItemAdapter(Context context, List data, int... layoutResId) {
        super(context, layoutResId[0], data);
        mTextLayoutResId = layoutResId[1];
    }

    @Override
    protected int getDefItemViewType(int position) {
        if (position % 2 == 0)
            return TEXT_TYPE;
        return super.getDefItemViewType(position);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TEXT_TYPE)
            return new TextViewHolder(getItemView(mTextLayoutResId, parent));
        return super.onCreateDefViewHolder(parent, viewType);
    }

    @Override
    protected void onBindDefViewHolder(BaseViewHolder holder, String item) {
        if (holder instanceof TextViewHolder)
            holder.setText(R.id.tv, item);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setImageUrl(R.id.iv, item);
    }

    public class TextViewHolder extends BaseViewHolder {

        public TextViewHolder(View itemView) {
            super(itemView.getContext(), itemView);
        }
    }

}
