package com.chad.library.adapter.base.listener;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * create by: allen on 16/8/3.
 */

public abstract class OnItemLongClickListener extends SimpleClickListener {

  @Override
  public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

  }

  @Override
  public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
    onSimpleItemLongClick(adapter, view, position);
  }

  @Override
  public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

  }

  @Override
  public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
  }

  public abstract void onSimpleItemLongClick(BaseQuickAdapter adapter, View view, int position);
}
