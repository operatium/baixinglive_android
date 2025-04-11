package com.baixingkuaizu.live.android.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.dialog.Baixing_PrivacyDialog
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.busiess.task.permission.Baixing_PermissionCheck
import com.baixingkuaizu.live.android.busiess.proxy.Baixing_ActivityProxy
import com.baixingkuaizu.live.android.busiess.task.privacyagreement.Baixing_PrivacyAgreementTaskManager
import com.baixingkuaizu.live.android.fragment.Baixing_SelectLoginFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 启动页活动，负责处理应用启动流程、隐私政策确认和权限申请
 */
@SuppressLint("CustomSplashScreen")
class Baixing_SplashActivity : Baixing_BaseActivity() {
    private val mBaixing_ActivityProxy = Baixing_ActivityProxy(this)

    private val mBaixing_localDataManager: Baixing_LocalDataManager
        get() = Baixing_LocalDataManager.baixing_getInstance(this)

    var mBaixing_privacyDialog: Baixing_PrivacyDialog? = null

    private val mBaixing_PermissionCheck = Baixing_PermissionCheck(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaixing_ActivityProxy.baixing_bind(this)
        setContentView(R.layout.baixing_splash_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.baixing_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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
            mBaixing_PermissionCheck.baixing_checkAndRequestPermissions(this) { isGranted ->
                baixing_startMainActivity(0)
            }
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
            supportFragmentManager.beginTransaction().run {
                add(R.id.baixing_framelayout, Baixing_SelectLoginFragment())
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                commit()
            }
        }
    }
}