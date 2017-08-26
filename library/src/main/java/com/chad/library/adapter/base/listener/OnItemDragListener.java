package com.chad.library.adapter.base.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by luoxw on 2016/6/20.
 */
public interface OnItemDragListener {

  void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos);

  void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to);

  void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos);
}
