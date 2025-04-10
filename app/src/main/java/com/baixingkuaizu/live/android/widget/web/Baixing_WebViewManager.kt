package com.baixingkuaizu.live.android.widget.web

import android.content.Context

object Baixing_WebViewManager {
    private val mBaixing_webViewMap = HashMap<String, Baixing_WebViewWrapper>()

    fun baixing_getOrCreateWebView(name: String, context: Context, url: String): Baixing_WebViewWrapper {
        return mBaixing_webViewMap.getOrPut(name) {
            Baixing_WebViewWrapper(context).apply {
                baixing_loadUrl(url)
            }
        }
    }

    fun baixing_getWebView(name: String): Baixing_WebViewWrapper? {
        return mBaixing_webViewMap[name]
    }

    fun baixing_updateWebView(name: String, url: String): Boolean {
        return mBaixing_webViewMap[name]?.let {
            it.baixing_loadUrl(url)
            true
        } ?: false
    }

    fun baixing_removeWebView(name: String) {
        mBaixing_webViewMap[name]?.baixing_destroy()
        mBaixing_webViewMap.remove(name)
    }

    fun baixing_clear() {
        mBaixing_webViewMap.forEach { (_, web) ->
            web.baixing_destroy()
        }
        mBaixing_webViewMap.clear()
    }
}