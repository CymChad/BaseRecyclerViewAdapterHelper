package com.allen.kotlinapp.util

import android.content.Context

/**
 * 文 件 名: Utils
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 11:55
 * 修改时间：
 * 修改备注：
 */

class Utils private constructor() {

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    companion object {
        @JvmField
        var context: Context? = null

        /**
         * 初始化工具类

         * @param context 上下文
         */
        fun init(context: Context) {
            Utils.context = context.applicationContext
        }

        /**
         * 获取ApplicationContext

         * @return ApplicationContext
         */
        @JvmStatic
        fun getContext(): Context {
            if (context != null) return context!!
            throw NullPointerException("u should init first")
        }
    }
}
