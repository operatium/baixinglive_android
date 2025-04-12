package com.baixingkuaizu.live.android.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.dialog.Baixing_PrivacyDialog
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.busiess.task.permission.Baixing_PermissionCheck
import com.baixingkuaizu.live.android.busiess.proxy.Baixing_ActivityProxy
import com.baixingkuaizu.live.android.busiess.task.privacyagreement.Baixing_PrivacyAgreementTaskManager
import com.baixingkuaizu.live.android.databinding.BaixingSplashActivityBinding
import com.baixingkuaizu.live.android.fragment.Baixing_SelectLoginFragment
import com.baixingkuaizu.live.android.widget.toast.CenterToast
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
        get() = Baixing_LocalDataManager.getInstance()

    var mBaixing_privacyDialog: Baixing_PrivacyDialog? = null

    private val mBaixing_PermissionCheck = Baixing_PermissionCheck(this)
    
    private lateinit var mBaixing_binding: BaixingSplashActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaixing_ActivityProxy.baixing_bind(this)
        mBaixing_binding = BaixingSplashActivityBinding.inflate(layoutInflater)
        setContentView(mBaixing_binding.root)
        mBaixing_binding.root.setWindowListener()
        lifecycleScope.launch {
            delay(100)
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
            CenterToast.show(this, "您需要同意隐私政策才能使用本应用")
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