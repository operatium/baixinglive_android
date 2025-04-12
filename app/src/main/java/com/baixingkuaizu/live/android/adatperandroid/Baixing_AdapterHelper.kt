package com.baixingkuaizu.live.android.adatperandroid

import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.view.View

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 适配器辅助类，提供点击事件防抖和尺寸转换功能
 */
object Baixing_AdapterHelper {
    private const val TAG = "yyx类Baixing_AdapterHelper"

    fun View.setClick(onClick: (View?) -> Unit) {
        setOnClickListener(Baixing_DebounceClickListener(onClick))
    }

    fun Int.dp2px(resources: Resources): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            resources.getDimension(this),
            resources.displayMetrics
        ).toInt().also {
            Log.d(TAG, "${resources.getDimension(this)}.dp2px: $it")
        }
    }
}