package com.chad.library.adapter.base.entity;

public abstract class JSectionEntity implements SectionEntity {


    @Override
    public int getItemType() {

        if (isHeader()) {
            return SectionEntity.Companion.HEADER_TYPE;
        } else {
            return SectionEntity.Companion.NORMAL_TYPE;
        }

    }
}
