package com.baixingkuaizu.live.android.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.busiess.router.Baixing_GoRouter

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 基础活动类，所有Activity的父类，提供生命周期管理、窗口设置、震动反馈等通用功能。集成了GoRouter路由管理，在Activity的创建和销毁时进行回调，并提供了View扩展方法setWindowListener用于处理系统窗口插入，支持全面屏和刘海屏适配。项目中所有Activity都继承自此类以保持一致的行为。
 */
open class Baixing_BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Baixing_GoRouter.baixing_callOnCreate(this)
        enableEdgeToEdge()
    }

    override fun onDestroy() {
        super.onDestroy()
        Baixing_GoRouter.baixing_callOnDestroy(this)
    }

    fun vibrateOnce(time:Long = 200) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as android.os.VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            if (vibrator.hasVibrator()) {
                val vibrationEffect = VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            }
        } else {
            @Suppress("DEPRECATION")
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
            if (vibrator.hasVibrator()) {
                @Suppress("DEPRECATION")
                vibrator.vibrate(time)
            }
        }
    }

    fun View.setWindowListener() {
        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}