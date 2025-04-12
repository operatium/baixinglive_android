package com.baixingkuaizu.live.android.activity

import android.os.Bundle
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.databinding.BaixingTeenModeActivityBinding
import com.baixingkuaizu.live.android.fragment.Baixing_TeenModeFragment
import com.baixingkuaizu.live.android.fragment.Baixing_TeenPlayListFragment
import com.baixingkuaizu.live.android.dialog.Baixing_ExitDialog

/**
 * @author yuyuexing
 * @date: 2025/4/13
 * @description: 青少年模式活动页面
 */
class Baixing_TeenModeActivity : Baixing_BaseActivity() {

    private lateinit var mBaixing_binding: BaixingTeenModeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaixing_binding = BaixingTeenModeActivityBinding.inflate(layoutInflater)
        setContentView(mBaixing_binding.root)
        mBaixing_binding.root.setWindowListener()
        
        baixing_toTeenModeFragment()
    }

    fun baixing_toTeenModeFragment() {
        val fragment = Baixing_TeenModeFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.baixing_teen_mode_container, fragment)
            .commit()
    }

    fun baixing_toTeenPlayListFragment() {
        val fragment = Baixing_TeenPlayListFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.baixing_teen_mode_container, fragment)
            .commit()
    }
    
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        Baixing_ExitDialog.baixing_handleExit(this) {
            super.onBackPressed()
        }
    }
} 