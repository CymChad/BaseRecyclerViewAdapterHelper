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
package com.chad.baserecyclerviewadapterhelper

import android.app.Application
import com.chad.baserecyclerviewadapterhelper.utils.AppUtils

/**
 * 文 件 名: MyApplication
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppUtils.init(this)
    }
}