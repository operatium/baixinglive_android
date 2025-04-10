package com.baixingkuaizu.live.android.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.busiess.proxy.Baixing_ActivityProxy
import com.baixingkuaizu.live.android.databinding.BaixingWebActivityBinding
import com.baixingkuaizu.live.android.widget.web.Baixing_WebViewManager
import com.baixingkuaizu.live.android.widget.web.Baixing_WebViewWrapper

class Baixing_WebActivity : Baixing_BaseActivity() {
    private val mBaixing_activityProxy = Baixing_ActivityProxy(this)
    private lateinit var mBaixing_binding: BaixingWebActivityBinding
    private var mBaixing_webViewWrapper: Baixing_WebViewWrapper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaixing_activityProxy.baixing_bind(this)
        mBaixing_binding = BaixingWebActivityBinding.inflate(layoutInflater)
        setContentView(mBaixing_binding.root)
        val urlname = intent.getStringExtra("name")?:return
        val url = intent.getStringExtra("url")?:"https://www.2339.com"
        Baixing_WebViewManager.baixing_getOrCreateWebView(urlname, applicationContext, url).run {
            baixing_show(mBaixing_binding.baixingWebView)

            mBaixing_webViewWrapper = this
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mBaixing_webViewWrapper?.baixing_canGoBack() == true) {
                    mBaixing_webViewWrapper?.baixing_goBack()
                    return
                }
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mBaixing_webViewWrapper = null
        mBaixing_activityProxy.baixing_unbind()
    }
}