package com.chad.baserecyclerviewadapterhelper.data;


import com.chad.baserecyclerviewadapterhelper.entity.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：RecyclerAdapterViewDemo
 * 类描述：模拟数据
 * 创建人：Chad
 * 创建时间：16/4/9 下午9:32
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

}
