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
 * @description: 基础活动类，提供通用功能
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