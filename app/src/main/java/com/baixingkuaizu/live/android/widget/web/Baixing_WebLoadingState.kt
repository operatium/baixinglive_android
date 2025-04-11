package com.baixingkuaizu.live.android.widget.web

/*
 * @author yuyuexing
 * @date: 2025/4/11
 * @description:webview的状态
 */
data class Baixing_WebLoadingState(
    var mBaixing_url: String = "",
    var mBaixing_progress: Int = 0,
    var mBaixing_isLoading: Boolean = false,
    var mBaixing_isError: Boolean = false,
    var mBaixing_errorMessage: String? = null,
    var mBaixing_timestamp: Long = System.currentTimeMillis()
)
