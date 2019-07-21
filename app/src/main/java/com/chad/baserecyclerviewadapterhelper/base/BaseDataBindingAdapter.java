package com.chad.baserecyclerviewadapterhelper.base;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

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
        super(data);
    }

    public BaseDataBindingAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected BaseBindingViewHolder<B> createBaseViewHolder(View view) {
        return new BaseBindingViewHolder<>(view);
    }

    @Override
    protected BaseBindingViewHolder<B> createBaseViewHolder(ViewGroup parent, int layoutResId) {
        B b = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        View view;
        if (b == null) {
            view = getItemView(layoutResId, parent);
        } else {
            view = b.getRoot();
        }
        BaseBindingViewHolder<B> holder = new BaseBindingViewHolder<>(view);
        holder.setBinding(b);
        return holder;
    }

    @Override
    protected void convert(BaseBindingViewHolder<B> helper, T item) {
        convert(helper.getBinding(), item);
        helper.getBinding().executePendingBindings();
    }

    protected abstract void convert(B b, T item);
}
