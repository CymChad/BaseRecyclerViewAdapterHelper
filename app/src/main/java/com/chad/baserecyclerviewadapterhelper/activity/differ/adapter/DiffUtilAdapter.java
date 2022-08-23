package com.chad.baserecyclerviewadapterhelper.activity.differ.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.DiffEntity;
import com.chad.library.adapter.base.BaseDifferAdapter;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;

/**
 * Create adapter
 */
public class DiffUtilAdapter extends BaseDifferAdapter<DiffEntity, QuickViewHolder> {

    public DiffUtilAdapter() {
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
