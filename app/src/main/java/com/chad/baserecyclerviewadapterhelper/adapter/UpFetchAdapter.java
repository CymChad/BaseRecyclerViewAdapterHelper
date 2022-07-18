package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.databinding.ItemHeaderAndFooterBinding;
import com.chad.baserecyclerviewadapterhelper.entity.Movie;
import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * @author: limuyang
 * @date: 2019-12-06
 * @Description:
 */
public class UpFetchAdapter extends BaseQuickAdapter<Movie, UpFetchAdapter.VH> {


    static class VH extends RecyclerView.ViewHolder {

        ItemHeaderAndFooterBinding viewBinding;

        public VH(@NonNull ItemHeaderAndFooterBinding binding) {
            super(binding.getRoot());
            this.viewBinding = binding;
        }

        public VH(@NonNull ViewGroup parent) {
            this(ItemHeaderAndFooterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @NonNull
    @Override
    protected VH onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        return new VH(parent);
    }

    @Override
    protected void onBindViewHolder(@NonNull VH holder, int position, Movie item) {
        switch (holder.getLayoutPosition() %
                3) {
            case 0:
                holder.viewBinding.iv.setImageResource(R.mipmap.animation_img1);
                break;
            case 1:
                holder.viewBinding.iv.setImageResource(R.mipmap.animation_img2);
                break;
            case 2:
                holder.viewBinding.iv.setImageResource(R.mipmap.animation_img3);
                break;
            default:
                break;
        }
    }

}
