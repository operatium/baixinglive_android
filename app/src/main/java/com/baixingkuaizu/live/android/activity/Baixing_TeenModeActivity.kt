package com.baixingkuaizu.live.android.activity

import android.os.Bundle
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.databinding.BaixingTeenModeActivityBinding
import com.baixingkuaizu.live.android.fragment.Baixing_TeenModeFragment

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
        
        // 初始化视图
        baixing_initViews()
    }
    
    /**
     * 初始化视图
     */
    private fun baixing_initViews() {
        // 加载青少年模式Fragment
        baixing_loadTeenModeFragment()
    }
    
    /**
     * 加载青少年模式Fragment
     */
    private fun baixing_loadTeenModeFragment() {
        val fragment = Baixing_TeenModeFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.baixing_teen_mode_container, fragment)
            .commit()
    }
} 