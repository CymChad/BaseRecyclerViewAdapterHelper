package com.chad.baserecyclerviewadapterhelper.utils

import android.view.Window
import androidx.core.view.WindowCompat

/**
 * px 转 dp
 */
inline val Float.px2Dp: Float
    get() {
        return this / AppUtils.app.resources.displayMetrics.density
    }

/**
 * px 转 sp
 */
internal val Float.px2Sp: Float
    get() {
        return this / AppUtils.app.resources.displayMetrics.scaledDensity
    }

/**
 * dp 转 px
 */
inline val Int.dp: Int
    get() {
        return (this * AppUtils.app.resources.displayMetrics.density + 0.5f).toInt()
    }

/**
 * dp 转 px
 */
inline val Float.dp: Int
    get() {
        return (this * AppUtils.app.resources.displayMetrics.density + 0.5f).toInt()
    }

/**
 * dp 转 px
 */
inline val Int.dpF: Float
    get() {
        return this * AppUtils.app.resources.displayMetrics.density
    }

/**
 * dp 转 px
 */
inline val Float.dpF: Float
    get() {
        return this * AppUtils.app.resources.displayMetrics.density
    }

/**
 * dp 转 sp
 */
inline val Int.sp: Int
    get() {
        return (this * AppUtils.app.resources.displayMetrics.scaledDensity).toInt()
    }

/**
 * dp 转 sp
 */
inline val Float.sp: Int
    get() {
        return (this * AppUtils.app.resources.displayMetrics.scaledDensity).toInt()
    }

/**
 * 设置状态栏高亮模式
 */
inline var Window.statusBarLightMode: Boolean
    set(value) {
        WindowCompat.getInsetsController(this, decorView)?.isAppearanceLightStatusBars = value
    }
    get() {
        return WindowCompat.getInsetsController(this, decorView)?.isAppearanceLightStatusBars ?: false
    }