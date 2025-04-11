package com.baixingkuaizu.live.android.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.busiess.proxy.Baixing_ActivityProxy
import com.baixingkuaizu.live.android.busiess.task.privacyagreement.Baixing_PrivacyAgreementTaskManager
import com.baixingkuaizu.live.android.databinding.BaixingWebActivityBinding
import com.baixingkuaizu.live.android.widget.web.Baixing_WebViewWrapper

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: Web页面活动，用于展示网页内容，如隐私政策和用户协议
 */
class Baixing_WebActivity : Baixing_BaseActivity() {
    private val mBaixing_activityProxy = Baixing_ActivityProxy(this)
    private lateinit var mBaixing_binding: BaixingWebActivityBinding
    private var mBaixing_webViewWrapper: Baixing_WebViewWrapper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaixing_activityProxy.baixing_bind(this)
        mBaixing_binding = BaixingWebActivityBinding.inflate(layoutInflater)
        setContentView(mBaixing_binding.root)
        mBaixing_binding.root.setWindowListener()

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
        baixing_loadurl()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBaixing_webViewWrapper?.baixing_dismiss()
        mBaixing_activityProxy.baixing_unbind()
        mBaixing_binding.baixingWebView.removeAllViews()
    }

    private fun baixing_loadurl() {
        val urlname = intent.getStringExtra("name") ?: ""
        val url = intent.getStringExtra("url") ?: ""
        val taskName = intent.getStringExtra("task name") ?: ""
        val taskType = intent.getStringExtra("task type") ?: ""

        when (taskType) {
            "Baixing_PrivacyAgreementTaskManager" -> {
                mBaixing_webViewWrapper = Baixing_PrivacyAgreementTaskManager.show(taskName, mBaixing_binding.baixingWebView)
            }
        }
    }
}