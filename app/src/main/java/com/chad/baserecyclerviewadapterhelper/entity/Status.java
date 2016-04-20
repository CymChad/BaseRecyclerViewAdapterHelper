package com.chad.baserecyclerviewadapterhelper.entity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class Status {
    public boolean isRetweet;
    public String text;
    public String userName;
    public String userAvatar;
    public String createdAt;

    @Override
    public String toString() {
        return "Status{" +
                "isRetweet=" + isRetweet +
                ", text='" + text + '\'' +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
