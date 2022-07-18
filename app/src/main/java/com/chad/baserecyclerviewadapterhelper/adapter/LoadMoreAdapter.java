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
import com.chad.baserecyclerviewadapterhelper.databinding.LayoutAnimationBinding;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.baserecyclerviewadapterhelper.utils.SpannableStringUtils;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * @author: limuyang
 * @date: 2019-12-04
 * @Description:
 */
public class LoadMoreAdapter extends BaseQuickAdapter<Status, LoadMoreAdapter.VH> {

    static class VH extends RecyclerView.ViewHolder {

        LayoutAnimationBinding viewBinding;

        public VH(@NonNull LayoutAnimationBinding binding) {
            super(binding.getRoot());
            this.viewBinding = binding;
        }

        public VH(@NonNull ViewGroup parent) {
            this(LayoutAnimationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @NonNull
    @Override
    protected VH onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        return new VH(parent);
    }

    @Override
    protected void onBindViewHolder(@NonNull VH holder, int position, Status item) {
        switch (holder.getLayoutPosition() % 3) {
            case 0:
                holder.viewBinding.img.setImageResource(R.mipmap.animation_img1);
                break;
            case 1:
                holder.viewBinding.img.setImageResource(R.mipmap.animation_img2);
                break;
            case 2:
                holder.viewBinding.img.setImageResource(R.mipmap.animation_img3);
                break;
            default:
                break;
        }
        holder.viewBinding.tweetName.setText("Hoteis in Rio de Janeiro " + position + "  " + item.getUserName());

        String msg = "\"He was one of Australia's most of distinguished artistes, renowned for his portraits\"";
        holder.viewBinding.tweetText.setText(SpannableStringUtils.getBuilder(msg).append("landscapes and nedes").setClickSpan(clickableSpan).create());
        holder.viewBinding.tweetText.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private final ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            Tips.show("事件触发了 landscapes and nedes");
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getContext().getResources().getColor(R.color.clickspan_color));
            ds.setUnderlineText(true);
        }
    };
}
