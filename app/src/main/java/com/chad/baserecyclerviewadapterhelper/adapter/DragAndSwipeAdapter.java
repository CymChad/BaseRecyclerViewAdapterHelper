package com.chad.baserecyclerviewadapterhelper.adapter;


import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.dragswipe.DragAndSwipeAdapterImpl;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;

import java.util.List;

public class DragAndSwipeAdapter extends BaseQuickAdapter<String, QuickViewHolder> implements DragAndSwipeAdapterImpl {

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        return new QuickViewHolder(R.layout.item_draggable_view, parent);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder holder, int position, String item) {
        switch (holder.getLayoutPosition() % 3) {
            case 0:
                holder.setImageResource(R.id.iv_head, R.mipmap.head_img0);
                break;
            case 1:
                holder.setImageResource(R.id.iv_head, R.mipmap.head_img1);
                break;
            case 2:
                holder.setImageResource(R.id.iv_head, R.mipmap.head_img2);
                break;
            default:
                break;
        }
        holder.setText(R.id.tv, item);
    }


    @NonNull
    @Override
    public RecyclerView.Adapter<?> getDragAndSwipeAdapter() {
        return this;
    }

    @NonNull
    @Override
    public List<?> getDragAndSwipeData() {
        return getItems();
    }
}
