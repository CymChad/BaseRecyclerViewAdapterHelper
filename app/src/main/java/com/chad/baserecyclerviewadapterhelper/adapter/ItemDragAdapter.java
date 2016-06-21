package com.chad.baserecyclerviewadapterhelper.adapter;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by luoxw on 2016/6/20.
 */
public class ItemDragAdapter extends BaseQuickAdapter<String> {
    public ItemDragAdapter(List data) {
        super(R.layout.item_draggable_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv, item);
    }
}
