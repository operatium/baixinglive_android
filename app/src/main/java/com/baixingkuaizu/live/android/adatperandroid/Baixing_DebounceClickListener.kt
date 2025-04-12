package com.baixingkuaizu.live.android.adatperandroid

import android.view.View
import android.view.View.OnClickListener

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 防抖点击监听器，防止用户快速多次点击导致的重复操作
 */
class Baixing_DebounceClickListener(
    private val mBaixing_delegate: (View?) -> Unit,
    private val mBaixing_interval: Long = 500
) : OnClickListener {
    private var mBaixing_lastClickTime: Long = 0

    override fun onClick(v: View?) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - mBaixing_lastClickTime >= mBaixing_interval) {
            mBaixing_lastClickTime = currentTime
            mBaixing_delegate.invoke(v)
        }
    }
}
