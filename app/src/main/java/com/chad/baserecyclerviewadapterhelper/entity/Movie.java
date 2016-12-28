package com.chad.baserecyclerviewadapterhelper.entity;

/**
 * Created by luoxiongwen on 16/10/24.
 */

public class Movie implements Cloneable {

    public static final String KEY_PRICE = "key_price";
    public static final String KEY_LENGTH = "key_length";

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (length != movie.length) return false;
        if (price != movie.price) return false;
        return name != null ? name.equals(movie.name) : movie.name == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + length;
        result = 31 * result + price;
        return result;
    }

    @Override
    public Movie clone() {
        Movie clone = null;
        try {
            clone = (Movie) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);  // won't happen
        }
        return clone;
    }
}
