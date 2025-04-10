package com.baixingkuaizu.live.android.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.dialog.Baixing_PrivacyDialog
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.busiess.permission.Baixing_PermissionManager
import com.baixingkuaizu.live.android.busiess.proxy.Baixing_ActivityProxy
import com.baixingkuaizu.live.android.busiess.task.privacyagreement.Baixing_PrivacyAgreementTaskManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class Baixing_SplashActivity : Baixing_BaseActivity() {
    private val mBaixing_ActivityProxy = Baixing_ActivityProxy(this)

    private val mBaixing_localDataManager: Baixing_LocalDataManager
        get() = Baixing_LocalDataManager.baixing_getInstance(this)

    var mBaixing_privacyDialog: Baixing_PrivacyDialog? = null

    private val mBaixing_PermissionManager = Baixing_PermissionManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaixing_ActivityProxy.baixing_bind(this)
        setContentView(R.layout.baixing_splash_activity)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            delay(100)
            // 检查用户是否已同意隐私政策
            if (mBaixing_localDataManager.baixing_isPrivacyAgreed()) {
                baixing_startMainActivity()
            } else {
                baixing_showPrivacyDialog()
            }
            Baixing_PrivacyAgreementTaskManager.createTask(this@Baixing_SplashActivity.applicationContext)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBaixing_ActivityProxy.baixing_unbind()
        mBaixing_privacyDialog?.dismiss()
    }

    private fun baixing_showPrivacyDialog() {
        mBaixing_privacyDialog?.dismiss()
        mBaixing_privacyDialog = Baixing_PrivacyDialog(this)
        mBaixing_privacyDialog?.baixing_setOnAgreeListener {
            mBaixing_localDataManager.baixing_setPrivacyAgreed(true)
            mBaixing_PermissionManager.baixing_checkAndRequestPermissions()
        }

        mBaixing_privacyDialog?.baixing_setOnDisagreeListener {
            // 用户拒绝隐私政策
            Toast.makeText(this, "您需要同意隐私政策才能使用本应用", Toast.LENGTH_LONG).show()
            System.exit(0)
        }

        mBaixing_privacyDialog?.show()
    }
    
    private fun baixing_startMainActivity(delay: Long = 500) {
        lifecycleScope.launch {
            delay(delay)
            val intent = Intent(this@Baixing_SplashActivity, Baixing_MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}