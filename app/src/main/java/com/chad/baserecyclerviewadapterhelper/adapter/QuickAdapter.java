package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class QuickAdapter extends BaseQuickAdapter<Status> {
    public QuickAdapter(Context context) {
        super(context, R.layout.tweet, DataServer.getSampleData(100));
    }

    public QuickAdapter(Context context, int dataSize) {
        super(context, R.layout.tweet, DataServer.getSampleData(dataSize));
    }

    @Override
    protected void convert(BaseViewHolder helper, Status item) {
        helper.setText(R.id.tweetName, item.userName)
                .setText(R.id.tweetText, item.text)
                .setText(R.id.tweetDate, item.createdAt)
                .setImageUrl(R.id.tweetAvatar, item.userAvatar)
                .setVisible(R.id.tweetRT, item.isRetweet)
                .linkify(R.id.tweetText);
    }
}
