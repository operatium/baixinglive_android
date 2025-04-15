package com.baixingkuaizu.live.android.busiess.proxy

import com.baixingkuaizu.live.android.base.Baixing_BaseFragment

class Baixing_FragmentProxy<T : Baixing_BaseFragment>(private var mBaixing_fragment: T?) {

    fun getFragment(): T? {
        return mBaixing_fragment
    }

    fun bind(fragment: T) {
        mBaixing_fragment = fragment
    }

    fun unbind() {
        mBaixing_fragment = null
    }
}