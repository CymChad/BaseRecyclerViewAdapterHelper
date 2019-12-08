package com.chad.baserecyclerviewadapterhelper.entity.section;

import com.chad.library.adapter.base.entity.NSectionEntity;
import com.chad.library.adapter.base.entity.SectionExpandableEntity;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VideoHeaderEntity implements SectionExpandableEntity {

    private String content;

    public VideoHeaderEntity(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Nullable
    @Override
    public List<NSectionEntity> getSubItem() {
        return null;
    }

    @Nullable
    @Override
    public NSectionEntity getHeaderEntity() {
        return null;
    }

    @Nullable
    @Override
    public NSectionEntity getFooterEntity() {
        return null;
    }

//    @Override
//    public boolean isHaveSubItem() {
//        return false;
//    }


    private boolean isExpanded = true;

    @Override
    public boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }
}
