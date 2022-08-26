package com.chad.baserecyclerviewadapterhelper.utils

import android.view.Window
import androidx.core.view.WindowCompat


/**
 * 设置状态栏高亮模式
 */
inline var Window.statusBarLightMode: Boolean
    set(value) {
        WindowCompat.getInsetsController(this, decorView).isAppearanceLightStatusBars = value
    }
    get() {
        return WindowCompat.getInsetsController(this, decorView).isAppearanceLightStatusBars
    }


/**
 * dp 转 px
 */
inline val Int.dp: Int
    get() {
        return (this * AppUtils.app.resources.displayMetrics.density + 0.5f).toInt()
    }