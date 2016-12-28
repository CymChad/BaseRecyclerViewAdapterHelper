package com.chad.baserecyclerviewadapterhelper.adapter;

import android.os.Bundle;
import android.util.Log;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.Movie;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 文 件 名: PullToRefreshAdapter 创 建 人: Allen 创建日期: 16/12/24 19:55 邮   箱: AllenCoder@126.com 修改时间：
 * 修改备注：
 */
public class DiffUtilAdapter extends BaseQuickAdapter<Movie, BaseViewHolder> {
    public DiffUtilAdapter() {
        super(R.layout.layout_animation, DataServer.getInstance().getMovieData());
    }


    @Override
    protected void convert(BaseViewHolder helper, Movie item) {
        //don't  need anything
    }

    @Override
    protected void convert(BaseViewHolder helper, Movie item, List payloads) {
        helper.addOnClickListener(R.id.img).addOnClickListener(R.id.tweetText).addOnClickListener(R.id.tweetName);
        switch (helper.getLayoutPosition() %
                3) {
            case 0:
                helper.setImageResource(R.id.img, R.mipmap.animation_img1);
                break;
            case 1:
                helper.setImageResource(R.id.img, R.mipmap.animation_img2);
                break;
            case 2:
                helper.setImageResource(R.id.img, R.mipmap.animation_img3);
                break;
        }
        helper.setText(R.id.tweetName, item.name);
        if (payloads.isEmpty()) {
            helper.setText(R.id.tweetText, item.name + item.length + ":  $" + item.price);
            helper.setText(R.id.tweetDate, item.price + "");
        } else {
            Bundle payload = (Bundle) payloads.get(0);
            helper.setText(R.id.tweetText, item.name + payload.getInt(Movie.KEY_LENGTH) + ":  $" + item.price);
            helper.setText(R.id.tweetDate, payload.getInt(Movie.KEY_PRICE)+"");
            Log.i(TAG, " movie:" + item.name + " updated  from payload!");
        }

    }
}
