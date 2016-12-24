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
package com.chad.baserecyclerviewadapterhelper;

import android.app.Application;

import com.chad.baserecyclerviewadapterhelper.util.Utils;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * 文 件 名: MyApplication
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class MyApplication extends Application {
    private static MyApplication appContext;

    public static MyApplication getInstance() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext =this;
        Utils.init(appContext);
        if (BuildConfig.DEBUG) {
            Logger
                    .init("BaseRecyclerViewAdapter")                 // default PRETTYLOGGER or use just init()
                    .methodCount(3)                 // default 2
                    .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                    .methodOffset(2)                // default 0
            ; //default AndroidLogAdapter


        }
    }
}
