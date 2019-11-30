package com.chad.baserecyclerviewadapterhelper.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.JSectionEntity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JavaTestAdapter2 extends BaseSectionQuickAdapter<JSectionEntity, BaseViewHolder> {


    public JavaTestAdapter2(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder helper, @Nullable JSectionEntity item) {

    }
}
