package com.chad.baserecyclerviewadapterhelper.activity.dragswipe.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.activity.differ.adapter.DiffEntityCallback;
import com.chad.baserecyclerviewadapterhelper.entity.DiffEntity;
import com.chad.library.adapter.base.BaseDifferAdapter;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;

import java.util.List;

/**
 * Create adapter
 */
public class DiffDragAndSwipeAdapter extends BaseDifferAdapter<DiffEntity, QuickViewHolder> {

    public DiffDragAndSwipeAdapter(List<DiffEntity> list) {
        super(new DiffEntityCallback(), list);
    }

    public DiffDragAndSwipeAdapter() {
        super(new DiffEntityCallback());
    }


    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        return new QuickViewHolder(R.layout.layout_animation, parent);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder holder, int position, DiffEntity item) {
        holder.setText(R.id.tweetName, item.getTitle())
                .setText(R.id.tweetText, item.getContent())
                .setText(R.id.tweetDate, item.getDate());
    }

}
