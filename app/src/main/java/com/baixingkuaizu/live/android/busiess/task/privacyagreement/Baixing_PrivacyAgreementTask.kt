package com.baixingkuaizu.live.android.busiess.task.privacyagreement

import android.content.Context
import android.util.Log
import android.webkit.WebView
import android.widget.FrameLayout
import com.baixingkuaizu.live.android.busiess.task.Baixing_BaseTask
import com.baixingkuaizu.live.android.widget.web.Baixing_WebViewManager
import com.baixingkuaizu.live.android.widget.web.Baixing_WebViewWrapper

/*
    隐私协议任务线
    提前预加载
    用户登陆后释放
 */
/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 隐私协议任务类，负责处理隐私协议和用户协议的预加载、显示和销毁
 */
class Baixing_PrivacyAgreementTask(private val mBaixing_name:String, private val mBaixing_url:String)
    : Baixing_BaseTask(mBaixing_name) {
        private val TAG = "yyx类Baixing_PrivacyAgreementTask"

    fun baixing_preLoad(context: Context) {
        Baixing_WebViewManager.baixing_getOrCreateWebView(mBaixing_name,context.applicationContext, mBaixing_url)
    }

    fun baixing_show(frameLayout: FrameLayout):Baixing_WebViewWrapper? {
        val webWrapper = Baixing_WebViewManager.baixing_getWebView(mBaixing_name)
        webWrapper?.baixing_show(frameLayout)
        if (webWrapper == null) {
            Log.e(TAG, "baixing_show: baixing_getWebView(${mBaixing_name}) is null")
        }
        return webWrapper
    }

    fun baixing_destroy() {
        Baixing_WebViewManager.baixing_removeWebView(mBaixing_name)
    }
}