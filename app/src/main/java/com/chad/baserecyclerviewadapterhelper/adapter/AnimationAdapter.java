package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.baserecyclerviewadapterhelper.utils.ClickableMovementMethod;
import com.chad.baserecyclerviewadapterhelper.utils.SpannableStringUtils;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class AnimationAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {

    public AnimationAdapter() {
        super(DataServer.getSampleData(100));
    }

    @NonNull
    @Override
    protected BaseViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_animation, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull BaseViewHolder holder, int position, Status item) {
        switch (holder.getLayoutPosition() % 3) {
            case 0:
                holder.setImageResource(R.id.img, R.mipmap.animation_img1);
                break;
            case 1:
                holder.setImageResource(R.id.img, R.mipmap.animation_img2);
                break;
            case 2:
                holder.setImageResource(R.id.img, R.mipmap.animation_img3);
                break;
            default:
                break;
        }
        holder.setText(R.id.tweetName, "Hoteis in Rio de Janeiro");
        String msg = "\"He was one of Australia's most of distinguished artistes, renowned for his portraits\"";
        ((TextView) holder.getView(R.id.tweetText)).setText(SpannableStringUtils.getBuilder(msg).append("landscapes and nedes").setClickSpan(clickableSpan).create());
        ((TextView) holder.getView(R.id.tweetText)).setMovementMethod(ClickableMovementMethod.getInstance());
        ((TextView) holder.getView(R.id.tweetText)).setFocusable(false);
        ((TextView) holder.getView(R.id.tweetText)).setClickable(false);
        ((TextView) holder.getView(R.id.tweetText)).setLongClickable(false);
    }


    private final ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(@NonNull View widget) {
            Tips.show("事件触发了 landscapes and nedes");
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getContext().getResources().getColor(R.color.clickspan_color));
            ds.setUnderlineText(true);
        }
    };
}
