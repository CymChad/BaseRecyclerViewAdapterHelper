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

import static com.chad.baserecyclerviewadapterhelper.R.layout.item;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class QuickClickAdapter extends BaseQuickAdapter<Status> {
    public QuickClickAdapter(Context context) {
        super(context, DataServer.getSampleData(100));
    }

    public QuickClickAdapter(Context context, int dataSize) {
        super(context, DataServer.getSampleData(dataSize));
    }

    @Override protected int getLayoutResId() {
        return item;
    }


    @Override protected void onCreateListener(BaseViewHolder holder) {
        holder.listenerOnItemClick()
                .listenerOnItemLongClick()
                .listenerOnItemChildClick(R.id.tweetAvatar)
                .listenerOnItemChildClick(R.id.tweetName)
                .listenerOnItemChildLongClick(R.id.tweetText);
    }

    @Override protected void onBindViewHolder(BaseViewHolder holder, Status item) {
        holder.itemView.setBackgroundResource(R.drawable.card_click);
        holder.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setVisible(R.id.tweetRT, item.isRetweet());
        Glide.with(mContext).load(item.getUserAvatar()).crossFade().placeholder(R.mipmap.def_head).transform(new GlideCircleTransform(mContext)).into((ImageView) holder.getView(R.id.tweetAvatar));
    }
}
