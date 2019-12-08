package com.chad.baserecyclerviewadapterhelper.adapter.section;

import com.chad.baserecyclerviewadapterhelper.adapter.section.provider.SectionFooterProvider;
import com.chad.baserecyclerviewadapterhelper.adapter.section.provider.SectionHeaderProvider;
import com.chad.baserecyclerviewadapterhelper.adapter.section.provider.SectionItemProvider;
import com.chad.baserecyclerviewadapterhelper.entity.section.VideoFooterEntity;
import com.chad.baserecyclerviewadapterhelper.entity.section.VideoHeaderEntity;
import com.chad.baserecyclerviewadapterhelper.entity.section.VideoItemEntity;
import com.chad.library.adapter.base.BaseSectionAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.NSectionEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Section2Adapter extends BaseSectionAdapter<BaseViewHolder> {

    public Section2Adapter() {
        super();
        addSectionHeaderProvider(new SectionHeaderProvider());
        addSectionItemProvider(new SectionItemProvider());
        addSectionFooterProvider(new SectionFooterProvider());
    }

    @Override
    protected int getItemType(@NotNull List<? extends NSectionEntity> data, int position) {
        NSectionEntity entity = data.get(position);
        if (entity instanceof VideoHeaderEntity) {
            return 0;
        } else if (entity instanceof VideoFooterEntity) {
            return 2;
        } else if (entity instanceof VideoItemEntity) {
            return 1;
        }

        return -1;
    }
}
