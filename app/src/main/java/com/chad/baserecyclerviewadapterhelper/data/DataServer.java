package com.chad.baserecyclerviewadapterhelper.data;


import com.chad.baserecyclerviewadapterhelper.entity.MySection;
import com.chad.baserecyclerviewadapterhelper.entity.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class DataServer {
    public static List<Status> getSampleData(int lenth) {
        List<Status> list = new ArrayList<>();
        for (int i = 0; i < lenth; i++) {
            Status status = new Status();
            status.setUserName("Chad"+i);
            status.setCreatedAt("04/05/"+i);
            status.setRetweet(i%2==0);
            status.setUserAvatar("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460");
            status.setText("Powerful and flexible RecyclerAdapter https://github.com/CymChad/BaseRecyclerViewAdapterHelper");
            list.add(status);
        }
        return list;
    }
    public static List<Status> addData(List list, int dataSize) {
        for (int i = 0; i < dataSize; i++) {
            Status status = new Status();
            status.setUserName("Chad"+i);
            status.setCreatedAt("04/05/"+i);
            status.setRetweet(i%2==0);
            status.setUserAvatar("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460");
            status.setText("Powerful and flexible RecyclerAdapter https://github.com/CymChad/BaseRecyclerViewAdapterHelper");
            list.add(status);
        }
        return list;
    }
    public static List<MySection> getSampleData() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(true,"2016/4/8",true));
        list.add(new MySection("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460"));
        list.add(new MySection("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460"));
        list.add(new MySection("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460"));
        list.add(new MySection("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460"));
        list.add(new MySection(true,"2016/4/7",false));
        list.add(new MySection("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460"));
        list.add(new MySection("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460"));
        list.add(new MySection("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460"));
        list.add(new MySection(true,"2016/4/6",false));
        list.add(new MySection("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460"));
        list.add(new MySection("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460"));
        return list;
    }

}
