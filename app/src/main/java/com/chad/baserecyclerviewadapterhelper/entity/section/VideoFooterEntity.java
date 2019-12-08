package com.chad.baserecyclerviewadapterhelper.entity.section;

import com.chad.library.adapter.base.entity.NSectionEntity;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VideoFooterEntity implements NSectionEntity {

    private String content;

    public VideoFooterEntity(String content) {
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
}
