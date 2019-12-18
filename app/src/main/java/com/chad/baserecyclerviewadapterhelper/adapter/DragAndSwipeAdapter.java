package com.chad.baserecyclerviewadapterhelper.adapter;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chad.library.adapter.base.module.DraggableModule;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DragAndSwipeAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements DraggableModule {

    public DragAndSwipeAdapter(List<String> data) {
        super(R.layout.item_draggable_view, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, @Nullable String item) {
        switch (helper.getLayoutPosition() % 3) {
            case 0:
                helper.setImageResource(R.id.iv_head, R.mipmap.head_img0);
                break;
            case 1:
                helper.setImageResource(R.id.iv_head, R.mipmap.head_img1);
                break;
            case 2:
                helper.setImageResource(R.id.iv_head, R.mipmap.head_img2);
                break;
            default:
                break;
        }
        helper.setText(R.id.tv, item);
    }
}
