package com.chad.baserecyclerviewadapterhelper.adapter.section.provider;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.section.VideoItemEntity;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.NSectionEntity;
import com.chad.library.adapter.base.provider.BaseSectionItemProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SectionItemProvider extends BaseSectionItemProvider<BaseViewHolder> {

    @Override
    public int getItemViewType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_section_content;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable NSectionEntity data) {
        if (data == null) {
            return;
        }

        VideoItemEntity entity = (VideoItemEntity) data;
        helper.setImageResource(R.id.iv, entity.getImg());
        helper.setText(R.id.tv, entity.getName());
    }
}
