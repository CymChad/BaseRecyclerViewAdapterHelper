package com.chad.baserecyclerviewadapterhelper.entity;

public class Movie {

    public String name;
    public int length;
    public int price;
    public String content;

    public Movie(String name, int length, int price,String content) {
        this.length = length;
        this.name = name;
        this.price = price;
        this.content=content;
    }
}
