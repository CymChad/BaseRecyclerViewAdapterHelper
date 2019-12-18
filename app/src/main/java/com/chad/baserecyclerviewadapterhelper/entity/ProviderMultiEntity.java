package com.chad.baserecyclerviewadapterhelper.entity;

/**
 * @author: limuyang
 * @date: 2019-12-04
 * @Description:
 */
public class ProviderMultiEntity {
    public static final int TEXT = 1;
    public static final int IMG = 2;
    public static final int IMG_TEXT = 3;

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
