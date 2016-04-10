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
    public static List<Status> getSampleData() {
        List<Status> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Status status = new Status();
            status.setUserName("Chad"+i);
            status.setCreatedAt("04/05/"+i);
            status.setRetweet(i%2==0);
            status.setUserAvatar("http://img.my.csdn.net/uploads/201603/14/1457944500_2896.jpg");
            status.setText("Hello world! myblog http://blog.csdn.net/cym492224103");
            list.add(status);
        }
        return list;
    }

}
