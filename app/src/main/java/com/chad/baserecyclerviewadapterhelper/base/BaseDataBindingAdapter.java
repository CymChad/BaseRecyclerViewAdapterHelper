package com.chad.baserecyclerviewadapterhelper.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;


/**
 * Created by tysheng
 * Date: 2017/5/11 14:39.
 * Email: tyshengsx@gmail.com
 */

public abstract class BaseDataBindingAdapter<T, Binding extends ViewDataBinding> extends BaseQuickAdapter<T, BaseBindingViewHolder<Binding>> {


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
    protected BaseBindingViewHolder<Binding> createBaseViewHolder(View view) {
        return new BaseBindingViewHolder<>(view);
    }

    @Override
    protected BaseBindingViewHolder<Binding> createBaseViewHolder(ViewGroup parent, int layoutResId) {
        Binding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        View view;
        if (binding == null) {
            view = getItemView(layoutResId, parent);
        } else {
            view = binding.getRoot();
        }
        BaseBindingViewHolder<Binding> holder = new BaseBindingViewHolder<>(view);
        holder.setBinding(binding);
        return holder;
    }

    @Override
    protected void convert(BaseBindingViewHolder<Binding> helper, T item, int position) {
        convert(helper.getBinding(), item);
        helper.getBinding().executePendingBindings();
    }

    protected abstract void convert(Binding binding, T item);
}
