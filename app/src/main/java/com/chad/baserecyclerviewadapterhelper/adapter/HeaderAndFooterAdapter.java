package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.databinding.ItemHeaderAndFooterBinding;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class HeaderAndFooterAdapter extends BaseQuickAdapter<Status, HeaderAndFooterAdapter.VH> {

    public HeaderAndFooterAdapter(List<Status> list) {
        super(list);
    }

    static class VH extends RecyclerView.ViewHolder {

        ItemHeaderAndFooterBinding binding;

        public VH(@NonNull ItemHeaderAndFooterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    protected HeaderAndFooterAdapter.VH onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        ItemHeaderAndFooterBinding binding = ItemHeaderAndFooterBinding.inflate(LayoutInflater.from(context), parent, false);
        return new VH(binding);
    }

    @Override
    protected void onBindViewHolder(@NonNull HeaderAndFooterAdapter.VH holder, int position, Status item) {
        switch (holder.getLayoutPosition() % 3) {
            case 0:
                holder.binding.iv.setImageResource(R.mipmap.animation_img1);
                break;
            case 1:
                holder.binding.iv.setImageResource(R.mipmap.animation_img2);
                break;
            case 2:
                holder.binding.iv.setImageResource(R.mipmap.animation_img3);
                break;
            default:
                break;
        }
    }

}
