package com.baixingkuaizu.live.android.busiess.privacyagreement

import android.content.Context
import android.widget.FrameLayout
import com.baixingkuaizu.live.android.base.Baixing_BaseTask
import com.baixingkuaizu.live.android.widget.web.Baixing_WebViewManager

/*
    隐私协议任务线
    提前预加载
    用户登陆后释放
 */
class Baixing_PrivacyAgreementTask(private val mBaixing_name:String, private val mBaixing_url:String)
    : Baixing_BaseTask(mBaixing_name) {

    fun baixing_preLoad(context: Context) {
        Baixing_WebViewManager.baixing_getOrCreateWebView(mBaixing_name,context.applicationContext, mBaixing_url)
    }

    fun baixing_show(frameLayout: FrameLayout) {
        Baixing_WebViewManager.baixing_getWebView(mBaixing_name)?.baixing_show(frameLayout)
    }

    fun baixing_destroy() {
        Baixing_WebViewManager.baixing_removeWebView(mBaixing_name)
    }
}