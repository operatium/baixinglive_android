package com.baixingkuaizu.live.android.busiess.proxy

import com.baixingkuaizu.live.android.base.Baixing_BaseActivity

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: Activity代理类
 */
class Baixing_ActivityProxy<T: Baixing_BaseActivity>(private var mBaixing_Activity: T?) {

    fun baixing_bind(activity: T) {
        mBaixing_Activity = activity
    }

    fun baixing_unbind() {
        mBaixing_Activity = null
    }

    fun baixing_getActivity():T? {
        return mBaixing_Activity
    }
}