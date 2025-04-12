package com.baixingkuaizu.live.android.base

import android.view.View
import androidx.fragment.app.Fragment
import com.baixingkuaizu.live.android.adatperandroid.Baixing_DebounceClickListener

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 基础Fragment类，所有Fragment的父类，提供通用功能和扩展方法。定义了View扩展方法setClick，为View添加防抖动点击监听器，避免用户快速多次点击导致的问题。项目中所有Fragment都继承自此类以保持一致的行为和体验。
 */
open class Baixing_BaseFragment:Fragment() {

    fun View.setClick(onClick: (View?) -> Unit) {
        setOnClickListener(Baixing_DebounceClickListener(onClick))
    }
}