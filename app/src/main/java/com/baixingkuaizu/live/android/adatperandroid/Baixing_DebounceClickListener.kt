package com.baixingkuaizu.live.android.adatperandroid

import android.view.View
import android.view.View.OnClickListener

class Baixing_DebounceClickListener(
    private val mBaixing_delegate: (View?) -> Unit,
    private val mBaixing_interval: Long = 500
) : OnClickListener {
    private var mBaixing_lastClickTime: Long = 0

    override fun onClick(v: View?) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - mBaixing_lastClickTime >= mBaixing_interval) {
            mBaixing_lastClickTime = currentTime
            // 模拟点击事件处理
            mBaixing_delegate.invoke(v)
        }
    }
}
