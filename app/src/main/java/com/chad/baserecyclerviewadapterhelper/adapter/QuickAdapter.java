package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * 项目名称：RecyclerAdapterViewDemo
 * 类描述：快速写法
 * 创建人：Chad
 * 创建时间：16/4/9 下午10:31
 */
public class QuickAdapter extends BaseQuickAdapter<Status> {
    public QuickAdapter(Context context) {
        super(context, R.layout.tweet, DataServer.getSampleData());
    }

    @Override
    protected void convert(BaseViewHolder helper, Status item) {
        helper.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setImageUrl(R.id.tweetAvatar, item.getUserAvatar())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .linkify(R.id.tweetText);
    }
}
