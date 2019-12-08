package com.chad.baserecyclerviewadapterhelper.entity.section;

import com.chad.library.adapter.base.entity.NSectionEntity;
import com.chad.library.adapter.base.entity.SectionExpandableEntity;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VideoEntity implements SectionExpandableEntity {

    private List<NSectionEntity> videoItems;

    private NSectionEntity header;

    private NSectionEntity footer = null;

    public VideoEntity(List<NSectionEntity> videoItems) {
        this.videoItems = videoItems;
    }


    public void setHeader(NSectionEntity header) {
        this.header = header;
    }


    public void setFooter(NSectionEntity footer) {
        this.footer = footer;
    }

    @Nullable
    @Override
    public List<NSectionEntity> getSubItem() {
        return videoItems;
    }

    @Nullable
    @Override
    public NSectionEntity getHeaderEntity() {
        return this.header;
    }

    @Nullable
    @Override
    public NSectionEntity getFooterEntity() {
        return this.footer;
    }

//    @Override
//    public boolean isHaveSubItem() {
//        return true;
//    }

    //    @Override
//    public boolean isHaveSectionHeader() {
//        return true;
//    }
//
//    @Override
//    public boolean isHaveSectionFooter() {
//        return true;
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
