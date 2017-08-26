package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class Level0Item extends AbstractExpandableItem<Level1Item> implements MultiItemEntity {

  public String title;
  public String subTitle;

  public Level0Item(String title, String subTitle) {
    this.subTitle = subTitle;
    this.title = title;
  }

  @Override
  public int getItemType() {
    return ExpandableItemAdapter.TYPE_LEVEL_0;
  }

  @Override
  public int getLevel() {
    return 0;
  }
}
