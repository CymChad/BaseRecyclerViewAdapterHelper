package com.chad.baserecyclerviewadapterhelper.entity;

/**
 * Created by luoxiongwen on 16/10/24.
 */

public class Movie {

    public String name;
    public int length;
    public int price;

    public Movie(String name, int length, int price) {
        this.length = length;
        this.name = name;
        this.price = price;
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
}
