package com.chad.baserecyclerviewadapterhelper.utils

import android.app.Application

object AppUtils {
    private lateinit var mApplication: Application

    val app: Application get() = mApplication

    fun init(application: Application) {
        mApplication = application
    }

}