package com.chad.baserecyclerviewadapterhelper.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.module.LoadMoreModule;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author: limuyang
 * @date: 2019-11-29
 * @Description:
 */
public class JavaTestAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements LoadMoreModule {


    public JavaTestAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, @Nullable String item) {

    }

}

