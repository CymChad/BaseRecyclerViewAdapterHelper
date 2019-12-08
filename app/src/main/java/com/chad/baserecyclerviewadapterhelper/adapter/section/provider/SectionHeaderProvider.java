package com.chad.baserecyclerviewadapterhelper.adapter.section.provider;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.section.VideoHeaderEntity;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.NSectionEntity;
import com.chad.library.adapter.base.provider.BaseSectionItemProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SectionHeaderProvider extends BaseSectionItemProvider<BaseViewHolder> {

    @Override
    public int getItemViewType() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.def_section_head;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable NSectionEntity data) {
        if (data == null) {
            return;
        }

        VideoHeaderEntity entity = (VideoHeaderEntity) data;
        helper.setText(R.id.header, entity.getContent());
    }
}
