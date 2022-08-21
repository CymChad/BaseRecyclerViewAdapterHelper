package com.chad.baserecyclerviewadapterhelper.utils

import android.app.Service
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * 震动类
 */
object VibratorUtils {
    /**
     * * 轻微的震动提醒
     */
    fun vibrate(context: Context) {
        if (Build.VERSION.SDK_INT >= 31) {
            // android 12 及以上使用新的 VibratorManager，创建 EFFECT_TICK 轻微震动（需要线性震动马达硬件支持）
            val manager: VibratorManager =
                context.getSystemService<VibratorManager>(VibratorManager::class.java)
            manager.defaultVibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
        } else if (Build.VERSION.SDK_INT >= 29) {
            // android 10 及以上使用原 Vibrator，创建 EFFECT_TICK 轻微震动（需要线性震动马达硬件支持）
            val vib = context.getSystemService<Vibrator>(Vibrator::class.java) as Vibrator
            vib.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
        } else {
            // 10 以下的系统，没有系统 API 驱动线性震动马达，只能创建普通震动
            val vib = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator //震动70毫秒
            vib.vibrate(70)
        }
    }
}