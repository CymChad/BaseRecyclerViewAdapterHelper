package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.databinding.LayoutNestItemBinding;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.baserecyclerviewadapterhelper.utils.SpannableStringUtils;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.baserecyclerviewadapterhelper.utils.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class NestAdapter extends BaseQuickAdapter<Status, NestAdapter.VH> {
    public NestAdapter() {
        super(DataServer.getSampleData(20));
        addChildClickViewIds(R.id.tweetText);
    }

    class VH extends RecyclerView.ViewHolder {
        public final LayoutNestItemBinding viewBinding;

        public VH(@NonNull LayoutNestItemBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }
    }

    @NonNull
    @Override
    protected VH onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        LayoutNestItemBinding viewBinding = LayoutNestItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new VH(viewBinding);
    }

    @Override
    protected void onBindViewHolder(@NonNull VH holder, int position, Status item) {
        switch (holder.getLayoutPosition() % 3) {
            case 0:
                holder.viewBinding.img.setImageResource(R.mipmap.animation_img1);
                break;
            case 1:
                holder.viewBinding.img.setImageResource( R.mipmap.animation_img2);
                break;
            case 2:
                holder.viewBinding.img.setImageResource( R.mipmap.animation_img3);
                break;
            default:
                break;
        }
        holder.viewBinding.tweetName.setText( "Hoteis in Rio de Janeiro");

        String msg = "\"He was one of Australia's most of distinguished artistes, renowned for his portraits\"";
        holder.viewBinding.tweetText.setText(SpannableStringUtils.getBuilder(msg).append("landscapes and nedes").setClickSpan(clickableSpan).create());
        holder.viewBinding.tweetText.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            Tips.show("事件触发了 landscapes and nedes");
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Utils.getContext().getResources().getColor(R.color.clickspan_color));
            ds.setUnderlineText(true);
        }
    };
}
