package com.allen.kotlinapp

import android.app.Application
import com.allen.kotlinapp.util.Utils
import com.orhanobut.logger.LogLevel
import com.orhanobut.logger.Logger

/**
 * 文 件 名: MyApplication
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 11:54
 * 修改时间：
 * 修改备注：
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Utils.init(this)
        if (BuildConfig.DEBUG) {
            Logger
                    .init("BaseRecyclerViewAdapter")                 // default PRETTYLOGGER or use just init()
                    .methodCount(3)                 // default 2
                    .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                    .methodOffset(2)                // default 0
            //default AndroidLogAdapter


        }
    }

    companion object {
        var instance: MyApplication? = null
            private set
    }
}
