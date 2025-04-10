package com.baixingkuaizu.live.android.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.busiess.proxy.Baixing_ActivityProxy
import com.baixingkuaizu.live.android.databinding.BaixingLoginActivityBinding
import com.baixingkuaizu.live.android.fragment.Baixing_LoginFragment

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 登录活动页面
 */
class Baixing_LoginActivity:Baixing_BaseActivity() {
    private val TAG = "Baixing_LoginActivity"

    private val mBaixing_activityProxy = Baixing_ActivityProxy(this)

    private lateinit var mBaixing_binding: BaixingLoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaixing_activityProxy.baixing_bind(this)
        mBaixing_binding = BaixingLoginActivityBinding.inflate(layoutInflater)
        setContentView(mBaixing_binding.root)
        mBaixing_binding.root.setWindowListener()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val fragmentManager = supportFragmentManager
                    if (fragmentManager.backStackEntryCount > 1) {
                        fragmentManager.popBackStack()
                    } else {
                        if (isTaskRoot) {
                            moveTaskToBack(true)
                        } else {
                            finish()
                        }
                    }
                }
            }
        )
        baixing_toLoginFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBaixing_activityProxy.baixing_unbind()
    }

    fun baixing_toLoginFragment() {
        supportFragmentManager.beginTransaction().run {
            add(R.id.baixing_framelayout2, Baixing_LoginFragment())
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            commit()
        }
    }
}