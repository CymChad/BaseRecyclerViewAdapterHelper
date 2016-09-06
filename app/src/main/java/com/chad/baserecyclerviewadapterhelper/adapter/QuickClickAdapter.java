package com.chad.baserecyclerviewadapterhelper.adapter;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class QuickClickAdapter extends BaseQuickAdapter<Status> {
    public QuickClickAdapter() {
        super( R.layout.item2, DataServer.getSampleData(100));
    }

    public QuickClickAdapter(int dataSize) {
        super( R.layout.item2, DataServer.getSampleData(dataSize));
    }

    public QuickClickAdapter(List<Status> data) {
        super( R.layout.item2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Status item) {
        helper.addOnClickListener(R.id.btn2).addOnClickListener(R.id.btn1);
        helper.setVisible(R.id.btn1,item.isShow()).setVisible(R.id.btn2,!item.isShow());
    }


}
