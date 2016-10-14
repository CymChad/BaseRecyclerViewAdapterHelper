package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.baserecyclerviewadapterhelper.transform.GlideCircleTransform;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.vh.BaseViewHolder;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class QuickAdapter extends BaseQuickAdapter<Status> {
    public QuickAdapter(Context context) {
        super(context, DataServer.getSampleData(10));
    }

    public QuickAdapter(Context context, int dataSize) {
        super(context, DataServer.getSampleData(dataSize));
    }

    @Override protected int getLayoutResId() {
        return R.layout.tweet;
    }


    @Override protected void onCreateListener(BaseViewHolder holder) {
        holder.listenerOnItemClick()
                .listenerOnItemChildClick(R.id.tweetAvatar)
                .listenerOnItemChildClick(R.id.tweetName);
    }

    @Override protected void onBindViewHolder(BaseViewHolder holder, Status item) {
        holder.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .linkify(R.id.tweetText);

        Glide.with(mContext).load(item.getUserAvatar()).crossFade().placeholder(R.mipmap.def_head).transform(new GlideCircleTransform(mContext)).into((ImageView) holder.getView(R.id.tweetAvatar));

    }
}
