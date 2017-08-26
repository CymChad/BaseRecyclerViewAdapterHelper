package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class Level1Item extends AbstractExpandableItem<Person> implements MultiItemEntity {

  public String title;
  public String subTitle;

  public Level1Item(String title, String subTitle) {
    this.subTitle = subTitle;
    this.title = title;
  }

  @Override
  public int getItemType() {
    return ExpandableItemAdapter.TYPE_LEVEL_1;
  }

  @Override
  public int getLevel() {
    return 1;
  }
}