package com.baixingkuaizu.live.android.widget.web

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout

class Baixing_WebViewWrapper(private val context: Context) {
    var mBaixing_webView: WebView? = null
    private var mBaixing_onPageFinishedListener: ((String?) -> Unit)? = null
    private var mBaixing_onPageStartedListener: ((String?) -> Unit)? = null
    private var mBaixing_onProgressChangedListener: ((Int) -> Unit)? = null
    private val mBaixing_loadingState = Baixing_WebLoadingState()

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
                    mBaixing_loadingState.apply {
                        mBaixing_url = url ?: ""
                        mBaixing_isLoading = true
                        mBaixing_isError = false
                        mBaixing_timestamp = System.currentTimeMillis()
                    }
                    mBaixing_onPageStartedListener?.invoke(url)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    mBaixing_loadingState.mBaixing_isLoading = false
                    mBaixing_onPageFinishedListener?.invoke(url)
                }

                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    super.onReceivedError(view, errorCode, description, failingUrl)
                    mBaixing_loadingState.apply {
                        mBaixing_isError = true
                        mBaixing_errorMessage = description
                    }
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    mBaixing_loadingState.mBaixing_progress = newProgress
                    mBaixing_onProgressChangedListener?.invoke(newProgress)
                }
            }
        }
    }

    fun baixing_loadUrl(url: String, webView: WebView = WebView(context.applicationContext)) {
        if (mBaixing_webView != webView) {
            mBaixing_webView?.destroy()
        }
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

    fun baixing_show(framelayout: FrameLayout) {
        framelayout.addView(mBaixing_webView)
    }

    fun baixing_dismiss() {
        mBaixing_webView?.parent?.let {
            val viewgroup: ViewGroup = it as ViewGroup
            viewgroup.removeView(mBaixing_webView)
        }
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

    fun baixing_getLoadingState(): Baixing_WebLoadingState = mBaixing_loadingState.copy()

    fun baixing_isLoading(): Boolean = mBaixing_loadingState.mBaixing_isLoading

    fun baixing_hasError(): Boolean = mBaixing_loadingState.mBaixing_isError

    fun baixing_getProgress(): Int = mBaixing_loadingState.mBaixing_progress

    fun baixing_getCurrentUrl(): String = mBaixing_loadingState.mBaixing_url
}