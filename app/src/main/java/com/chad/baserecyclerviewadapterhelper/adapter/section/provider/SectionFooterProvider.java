package com.chad.baserecyclerviewadapterhelper.adapter.section.provider;

import android.view.View;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.section.VideoFooterEntity;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.NSectionEntity;
import com.chad.library.adapter.base.provider.BaseSectionItemProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SectionFooterProvider extends BaseSectionItemProvider<BaseViewHolder> {

    public SectionFooterProvider() {
        addChildClickViewIds(R.id.footerTv);
    }

    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.section_footer;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable NSectionEntity data) {
        if (data == null) {
            return;
        }

        VideoFooterEntity entity = (VideoFooterEntity) data;
    }

    @Override
    public void onChildClick(@NotNull BaseViewHolder helper, @NotNull View view, NSectionEntity data, int position) {
        if (view.getId() == R.id.footerTv) {
            Tips.show("Footer Click");
        }
    }
}
