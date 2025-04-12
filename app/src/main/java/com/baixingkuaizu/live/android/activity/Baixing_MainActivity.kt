package com.baixingkuaizu.live.android.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.busiess.router.Baixing_GoRouter
import com.baixingkuaizu.live.android.databinding.BaixingMainActivityBinding
import com.baixingkuaizu.live.android.dialog.Baixing_TeenModeDialog
import com.baixingkuaizu.live.android.fragment.Baixing_TeenModeFragment

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 主活动页面
 */
class Baixing_MainActivity : Baixing_BaseActivity() {
    
    private lateinit var mBaixing_localDataManager: Baixing_LocalDataManager

    private lateinit var mBaixing_binding: BaixingMainActivityBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaixing_binding = BaixingMainActivityBinding.inflate(layoutInflater)
        setContentView(mBaixing_binding.root)
        mBaixing_binding.root.setWindowListener()
        
        baixing_initData()
        
        // 检查是否启用了青少年模式
        if (baixing_checkTeenModeEnabled()) {
            return // 如果已跳转到青少年模式，不再继续执行
        }
        
        // 如果未启用青少年模式，则检查是否需要显示青少年模式对话框
        baixing_showTeenModeDialogIfNeeded()
    }
    
    private fun baixing_initData() {
        mBaixing_localDataManager = Baixing_LocalDataManager.baixing_getInstance(this)
    }
    
    /**
     * 检查是否启用了青少年模式，如果启用则自动跳转到青少年模式界面
     * @return 是否已跳转到青少年模式界面
     */
    private fun baixing_checkTeenModeEnabled(): Boolean {
        if (mBaixing_localDataManager.baixing_isTeenModeEnabled()) {
            val intent = Intent(this, Baixing_TeenModeActivity::class.java)
            startActivity(intent)
            return true
        }
        return false
    }
    
    private fun baixing_showTeenModeDialogIfNeeded() {
        if (!mBaixing_localDataManager.baixing_isTeenModeDialogShown()) {
            val dialog = Baixing_TeenModeDialog(this)
            dialog.baixing_setOnEnterTeenModeListener {
                // 处理进入青少年模式的逻辑
                baixing_navigateToTeenModeActivity()
            }
            dialog.baixing_setOnDismissListener {
                // 用户点击"我知道了"按钮，记录显示时间
                mBaixing_localDataManager.baixing_setTeenModeDialogShown()
            }
            dialog.show()
        }
    }
    
    /**
     * 跳转到青少年模式Activity
     */
    private fun baixing_navigateToTeenModeActivity() {
        val intent = Intent(this, Baixing_TeenModeActivity::class.java)
        startActivity(intent)
    }
}