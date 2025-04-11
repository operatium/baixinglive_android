package com.baixingkuaizu.live.android.base

import android.view.View
import androidx.fragment.app.Fragment
import com.baixingkuaizu.live.android.adatperandroid.Baixing_DebounceClickListener

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 基础Fragment类
 */
open class Baixing_BaseFragment:Fragment() {

    fun View.setClick(onClick: (View?) -> Unit) {
        setOnClickListener(Baixing_DebounceClickListener(onClick))
    }
}