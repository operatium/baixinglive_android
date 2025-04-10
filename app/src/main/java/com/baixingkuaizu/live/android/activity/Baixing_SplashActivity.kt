package com.baixingkuaizu.live.android.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.dialog.Baixing_PrivacyDialog
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.busiess.proxy.Baixing_ActivityProxy
import com.baixingkuaizu.live.android.widget.web.Baixing_WebViewManager


@SuppressLint("CustomSplashScreen")
class Baixing_SplashActivity : Baixing_BaseActivity() {
    private val mBaixing_ActivityProxy = Baixing_ActivityProxy(this)

    private val mBaixing_localDataManager: Baixing_LocalDataManager
        get() = Baixing_LocalDataManager.baixing_getInstance(this)

    var mBaixing_privacyDialog: Baixing_PrivacyDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaixing_ActivityProxy.baixing_bind(this)
        setContentView(R.layout.baixing_splash_activity)

        // 检查用户是否已同意隐私政策
        if (mBaixing_localDataManager.baixing_isPrivacyAgreed()) {
            baixing_startMainActivity()
        } else {
            baixing_showPrivacyDialog()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mBaixing_ActivityProxy.baixing_unbind()
        mBaixing_privacyDialog?.dismiss()
    }

    private fun baixing_showPrivacyDialog() {
        mBaixing_privacyDialog= Baixing_PrivacyDialog(this)
        mBaixing_privacyDialog?.baixing_setOnAgreeListener {
            // 用户同意隐私政策
            mBaixing_localDataManager.baixing_setPrivacyAgreed(true)
            baixing_startMainActivity()
        }

        mBaixing_privacyDialog?.baixing_setOnDisagreeListener {
            // 用户拒绝隐私政策
            Toast.makeText(this, "您需要同意隐私政策才能使用本应用", Toast.LENGTH_LONG).show()
        }

        mBaixing_privacyDialog?.show()
    }
    
    private fun baixing_startMainActivity() {
        // 延迟一段时间后跳转到主页面
        val intent = Intent(this, Baixing_MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}