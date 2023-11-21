package com.chad.library.adapter4.dragswipe.listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by luoxw on 2016/6/20.
 */
public interface OnItemDragListener {
    void onItemDragStart(@Nullable RecyclerView.ViewHolder viewHolder, int pos);

    void onItemDragMoving(@NonNull RecyclerView.ViewHolder source, int from, @NonNull RecyclerView.ViewHolder target, int to);

    void onItemDragEnd(@NonNull RecyclerView.ViewHolder viewHolder, int pos);
}
