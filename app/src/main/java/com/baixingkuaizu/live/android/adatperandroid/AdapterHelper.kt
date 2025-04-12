package com.baixingkuaizu.live.android.adatperandroid

import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.view.View

object AdapterHelper {
    private const val TAG = "yyx类AdapterHelper"

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