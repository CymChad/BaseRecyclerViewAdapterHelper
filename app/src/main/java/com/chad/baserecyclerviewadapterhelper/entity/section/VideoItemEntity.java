package com.chad.baserecyclerviewadapterhelper.entity.section;

import androidx.annotation.DrawableRes;

import com.chad.library.adapter.base.entity.NSectionEntity;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VideoItemEntity implements NSectionEntity {

    private int img;
    private String name;

    public VideoItemEntity(@DrawableRes int img, String name) {
        this.img = img;
        this.name = name;
    }

    @DrawableRes
    public int getImg() {
        return img;
    }

    public void setImg(@DrawableRes int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * 没有子项目，返回null
     *
     * @return subItem
     */
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
