package com.chad.baserecyclerviewadapterhelper.activity.itemclick.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.databinding.ItemClickChildviewBinding;
import com.chad.baserecyclerviewadapterhelper.databinding.ItemClickViewBinding;
import com.chad.baserecyclerviewadapterhelper.databinding.ItemLongClickChildviewBinding;
import com.chad.baserecyclerviewadapterhelper.databinding.ItemLongClickViewBinding;
import com.chad.baserecyclerviewadapterhelper.entity.ClickEntity;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;

import java.util.List;

/**
 *
 */
public class ItemClickAdapter extends BaseMultiItemQuickAdapter<ClickEntity> {

    static class ItemViewVH extends RecyclerView.ViewHolder {

        ItemClickViewBinding viewBinding;

        public ItemViewVH(@NonNull ItemClickViewBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        public ItemViewVH(@NonNull ViewGroup parent) {
            this(ItemClickViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    static class ItemChildVH extends RecyclerView.ViewHolder {

        ItemClickChildviewBinding viewBinding;

        public ItemChildVH(@NonNull ItemClickChildviewBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        public ItemChildVH(@NonNull ViewGroup parent) {
            this(ItemClickChildviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    static class ItemLongClickVH extends RecyclerView.ViewHolder {

        ItemLongClickViewBinding viewBinding;

        public ItemLongClickVH(@NonNull ItemLongClickViewBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        public ItemLongClickVH(@NonNull ViewGroup parent) {
            this(ItemLongClickViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    static class ItemChildLongClickVH extends RecyclerView.ViewHolder {

        ItemLongClickChildviewBinding viewBinding;

        public ItemChildLongClickVH(@NonNull ItemLongClickChildviewBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        public ItemChildLongClickVH(@NonNull ViewGroup parent) {
            this(ItemLongClickChildviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    /**
     * 构造方法
     */
    public ItemClickAdapter(List<ClickEntity> data) {
        super(data);

        addItemType(ClickEntity.CLICK_ITEM_VIEW, ItemViewVH.class, new OnMultiItemAdapterListener<ClickEntity, ItemViewVH>() {

            @NonNull
            @Override
            public ItemViewVH onCreate(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
                return new ItemViewVH(parent);
            }

            @Override
            public void onBind(@NonNull ItemViewVH holder, int position, ClickEntity item) {
            }
        }).addItemType(ClickEntity.CLICK_ITEM_CHILD_VIEW, ItemChildVH.class, new OnMultiItemAdapterListener<ClickEntity, ItemChildVH>() {

            @NonNull
            @Override
            public ItemChildVH onCreate(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
                return new ItemChildVH(parent);
            }

            @Override
            public void onBind(@NonNull ItemChildVH holder, int position, ClickEntity item) {
            }
        }).addItemType(ClickEntity.LONG_CLICK_ITEM_VIEW, ItemLongClickVH.class, new OnMultiItemAdapterListener<ClickEntity, ItemLongClickVH>() {

            @NonNull
            @Override
            public ItemLongClickVH onCreate(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
                return new ItemLongClickVH(parent);
            }

            @Override
            public void onBind(@NonNull ItemLongClickVH holder, int position, ClickEntity item) {
            }
        }).addItemType(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW, ItemChildLongClickVH.class, new OnMultiItemAdapterListener<ClickEntity, ItemChildLongClickVH>() {

            @NonNull
            @Override
            public ItemChildLongClickVH onCreate(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
                return new ItemChildLongClickVH(parent);
            }

            @Override
            public void onBind(@NonNull ItemChildLongClickVH holder, int position, ClickEntity item) {
            }
        }).onItemViewType(new OnItemViewTypeListener<ClickEntity>() {
            @Override
            public int onItemViewType(int position, @NonNull List<? extends ClickEntity> list) {
                return list.get(position).getItemType();
            }
        });

    }


}
