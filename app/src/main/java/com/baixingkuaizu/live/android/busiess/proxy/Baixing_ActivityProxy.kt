package com.baixingkuaizu.live.android.busiess.proxy

import com.baixingkuaizu.live.android.base.Baixing_BaseActivity

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