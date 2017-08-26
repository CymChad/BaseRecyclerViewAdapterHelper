package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MultipleItem implements MultiItemEntity {

  public static final int TEXT = 1;
  public static final int IMG = 2;
  public static final int IMG_TEXT = 3;
  public static final int TEXT_SPAN_SIZE = 3;
  public static final int IMG_SPAN_SIZE = 1;
  public static final int IMG_TEXT_SPAN_SIZE = 4;
  public static final int IMG_TEXT_SPAN_SIZE_MIN = 2;
  private int itemType;
  private int spanSize;

  public MultipleItem(int itemType, int spanSize, String content) {
    this.itemType = itemType;
    this.spanSize = spanSize;
    this.content = content;
  }

  public MultipleItem(int itemType, int spanSize) {
    this.itemType = itemType;
    this.spanSize = spanSize;
  }

  public int getSpanSize() {
    return spanSize;
  }

  public void setSpanSize(int spanSize) {
    this.spanSize = spanSize;
  }

  private String content;

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public int getItemType() {
    return itemType;
  }
}
