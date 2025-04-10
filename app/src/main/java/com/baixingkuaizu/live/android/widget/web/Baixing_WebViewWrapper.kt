package com.baixingkuaizu.live.android.widget.web

import android.content.Context
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout

class Baixing_WebViewWrapper(private val context: Context) {
    private var mBaixing_webView: WebView? = null
    private var mBaixing_onPageFinishedListener: ((String?) -> Unit)? = null
    private var mBaixing_onPageStartedListener: ((String?) -> Unit)? = null
    private var mBaixing_onProgressChangedListener: ((Int) -> Unit)? = null

    private fun baixing_setupWebView(
        isJavaScriptEnabled: Boolean = false,
        isDomStorageEnabled: Boolean = true,
        isWideViewPortEnabled: Boolean = true,
        isLoadWithOverviewMode: Boolean = true,
        isSupportZoom: Boolean = true,
        isBuiltInZoomControls: Boolean = true,
        isDisplayZoomControls: Boolean = false,
        textEncodingName: String = "UTF-8"
    ) {
        mBaixing_webView?.apply {
            settings.apply {
                javaScriptEnabled = isJavaScriptEnabled
                domStorageEnabled = isDomStorageEnabled
                useWideViewPort = isWideViewPortEnabled
                loadWithOverviewMode = isLoadWithOverviewMode
                setSupportZoom(isSupportZoom)
                builtInZoomControls = isBuiltInZoomControls
                displayZoomControls = isDisplayZoomControls
                defaultTextEncodingName = textEncodingName
            }

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    mBaixing_onPageStartedListener?.invoke(url)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    mBaixing_onPageFinishedListener?.invoke(url)
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    mBaixing_onProgressChangedListener?.invoke(newProgress)
                }
            }
        }
    }

    fun baixing_loadUrl(url: String, webView: WebView=WebView(context.applicationContext)) {
        mBaixing_webView = webView
        baixing_setupWebView()
        mBaixing_webView?.loadUrl(url)
    }

    fun baixing_canGoBack(): Boolean {
        return mBaixing_webView?.canGoBack() ?: false
    }

    fun baixing_goBack() {
        if (baixing_canGoBack()) {
            mBaixing_webView?.goBack()
        }
    }

    fun baixing_show(framelayout:FrameLayout) {
        framelayout.addView(mBaixing_webView)
    }

    fun baixing_setOnPageFinishedListener(listener: (String?) -> Unit) {
        mBaixing_onPageFinishedListener = listener
    }

    fun baixing_setOnPageStartedListener(listener: (String?) -> Unit) {
        mBaixing_onPageStartedListener = listener
    }

    fun baixing_setOnProgressChangedListener(listener: (Int) -> Unit) {
        mBaixing_onProgressChangedListener = listener
    }

    fun baixing_destroy() {
        mBaixing_webView?.destroy()
        mBaixing_webView = null
        mBaixing_onPageFinishedListener = null
        mBaixing_onPageStartedListener = null
        mBaixing_onProgressChangedListener = null
    }
}