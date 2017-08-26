package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class Person implements MultiItemEntity {

  public Person(String name, int age) {
    this.age = age;
    this.name = name;
  }

  public String name;
  public int age;

  @Override
  public int getItemType() {
    return ExpandableItemAdapter.TYPE_PERSON;
  }
}