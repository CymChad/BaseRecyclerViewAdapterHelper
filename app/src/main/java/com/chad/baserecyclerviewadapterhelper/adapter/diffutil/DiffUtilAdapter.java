package com.chad.baserecyclerviewadapterhelper.adapter.diffutil;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.DiffUtilDemoEntity;
import com.chad.library.adapter.base.BaseDifferAdapter;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;

import java.util.List;

/**
 * Create adapter
 */
public class DiffUtilAdapter extends BaseDifferAdapter<DiffUtilDemoEntity, QuickViewHolder> {

    public DiffUtilAdapter(List<DiffUtilDemoEntity> list) {
        super(new DiffDemoCallback(), list);
    }

    public DiffUtilAdapter() {
        super(new DiffDemoCallback());
    }


    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        return new QuickViewHolder(R.layout.layout_animation, parent);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder holder, int position, DiffUtilDemoEntity item) {
        holder.setText(R.id.tweetName, item.getTitle())
                .setText(R.id.tweetText, item.getContent())
                .setText(R.id.tweetDate, item.getDate());
    }

}
