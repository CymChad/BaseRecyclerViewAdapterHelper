package com.chad.baserecyclerviewadapterhelper.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Created by tysheng
 * Date: 2017/5/11 14:39.
 * Email: tyshengsx@gmail.com
 */

public abstract class BaseDataBindingAdapter<T, B extends ViewDataBinding> extends BaseQuickAdapter<T, BaseBindingViewHolder<B>> {


    public BaseDataBindingAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public BaseDataBindingAdapter(@Nullable List<T> data) {
        super(0, data);
    }

    public BaseDataBindingAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

//    @NonNull
//    @Override
//    protected BaseBindingViewHolder<B> createBaseViewHolder(@NonNull View view) {
//        return new BaseBindingViewHolder<>(view);
//    }

    @NotNull
    @Override
    protected BaseBindingViewHolder<B> createBaseViewHolder(@NotNull ViewGroup parent, int layoutResId) {
        B b = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResId, parent, false);
        View view;
        if (b == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent);
        } else {
            view = b.getRoot();
        }
        BaseBindingViewHolder<B> holder = new BaseBindingViewHolder<>(view);
        holder.setBinding(b);
        return holder;
    }


    @Override
    protected void convert(@NonNull BaseBindingViewHolder<B> helper, T item) {
        convert(helper.getBinding(), item);
        helper.getBinding().executePendingBindings();
    }

    protected abstract void convert(B b, T item);
}
