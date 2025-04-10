package com.baixingkuaizu.live.android.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.busiess.proxy.Baixing_ActivityProxy
import com.baixingkuaizu.live.android.databinding.BaixingLoginActivityBinding
import com.baixingkuaizu.live.android.fragment.Baixing_LoginFragment

class Baixing_LoginActivity:Baixing_BaseActivity() {
    private val TAG = "Baixing_LoginActivity"

    private val mBaixing_activityProxy = Baixing_ActivityProxy(this)

    private lateinit var mBaixing_binding: BaixingLoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaixing_activityProxy.baixing_bind(this)
        mBaixing_binding = BaixingLoginActivityBinding.inflate(layoutInflater)
        setContentView(mBaixing_binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(mBaixing_binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val fragmentManager = supportFragmentManager
                    if (fragmentManager.backStackEntryCount > 1) {
                        fragmentManager.popBackStack()
                    } else {
                        finish()
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

    private fun baixing_toLoginFragment() {
        supportFragmentManager.beginTransaction().run {
            add(R.id.baixing_framelayout2, Baixing_LoginFragment())
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            commit()
        }
    }
}