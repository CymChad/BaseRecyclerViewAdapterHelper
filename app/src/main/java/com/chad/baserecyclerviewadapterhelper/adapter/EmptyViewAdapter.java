package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.databinding.LayoutAnimationBinding;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.library.adapter.base.BaseQuickAdapter;

public class EmptyViewAdapter extends BaseQuickAdapter<Status, EmptyViewAdapter.VH> {


    static class VH extends RecyclerView.ViewHolder {

        LayoutAnimationBinding binding;

        public VH(@NonNull LayoutAnimationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
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
                holder.binding.img.setImageResource(R.mipmap.animation_img1);
                break;
            case 1:
                holder.binding.img.setImageResource(R.mipmap.animation_img2);
                break;
            case 2:
                holder.binding.img.setImageResource(R.mipmap.animation_img3);
                break;
            default:
                break;
        }
        holder.binding.tweetName.setText("Hoteis in Rio de Janeiro");
        holder.binding.tweetText.setText("O ever youthful,O ever weeping");
    }

}
