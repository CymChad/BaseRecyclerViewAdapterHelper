package com.chad.baserecyclerviewadapterhelper.adapter;

import com.chad.library.adapter.base.BaseProviderMultiAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate;
import com.chad.library.adapter.base.entity.JSectionEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JavaTestAdapter2 extends BaseProviderMultiAdapter<JSectionEntity, BaseViewHolder> {


    public JavaTestAdapter2() {
        super();
    }

    @Override
    protected int getItemType(@NotNull List<? extends JSectionEntity> data, int position) {
        return 0;
    }

}
