package com.chad.baserecyclerviewadapterhelper.base;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by tysheng
 * Date: 2017/5/11 14:42.
 * Email: tyshengsx@gmail.com
 */

public class BaseBindingViewHolder<Binding extends ViewDataBinding,T> extends BaseViewHolder<T> {
    private Binding mBinding;

    public BaseBindingViewHolder(View view) {
        super(view);
    }

    public Binding getBinding() {
        return mBinding;
    }

    public void setBinding(Binding binding) {
        mBinding = binding;
    }
}
