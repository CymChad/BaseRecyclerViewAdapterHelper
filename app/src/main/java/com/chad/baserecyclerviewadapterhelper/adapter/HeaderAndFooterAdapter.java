package com.chad.baserecyclerviewadapterhelper.adapter;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class HeaderAndFooterAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {
    public HeaderAndFooterAdapter() {
        super( R.layout.item_header_and_footer, DataServer.getSampleData(100));
    }

    public HeaderAndFooterAdapter(int dataSize) {
        super( R.layout.item_header_and_footer, DataServer.getSampleData(dataSize));
    }

    @Override
    protected void convert(BaseViewHolder helper, Status item) {
    }


}
