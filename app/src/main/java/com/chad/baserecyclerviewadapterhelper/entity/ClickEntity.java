/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2015, Allen, china, shanghai
**                          All Rights Reserved
**
**                          
**                         
**-----------------------------------版本信息------------------------------------
** 版    本: V0.1
**
**------------------------------------------------------------------------------
********************************End of Head************************************\
*/
package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 文 件 名: ClickEntity
 * 创 建 人: Allen
 * 创建日期: 16/11/1 22:16
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class ClickEntity implements MultiItemEntity {
    public static final int CLICK_ITEM_VIEW = 1;
    public static final int CLICK_ITEM_CHILD_VIEW = 2;
    public static final int LONG_CLICK_ITEM_VIEW = 3;
    public static final int LONG_CLICK_ITEM_CHILD_VIEW = 4;
    public static final int NEST_CLICK_ITEM_CHILD_VIEW = 5;
    public int Type;

    public ClickEntity(final int type) {
        Type = type;
    }

    @Override
    public int getItemType() {
        return Type;
    }
}
