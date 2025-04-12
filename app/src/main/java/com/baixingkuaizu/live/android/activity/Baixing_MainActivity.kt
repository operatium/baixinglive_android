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
        baixing_showTeenModeDialogIfNeeded()
    }
    
    private fun baixing_initData() {
        mBaixing_localDataManager = Baixing_LocalDataManager.baixing_getInstance(this)
    }
    
    private fun baixing_showTeenModeDialogIfNeeded() {
        if (!mBaixing_localDataManager.baixing_isTeenModeDialogShown()) {
            val dialog = Baixing_TeenModeDialog(this)
            dialog.baixing_setOnEnterTeenModeListener {
                Baixing_GoRouter.baixing_jumpTeenModeActivity()
            }
            dialog.baixing_setOnDismissListener {
                // 用户点击"我知道了"按钮
                mBaixing_localDataManager.baixing_setTeenModeDialogShown(true)
            }
            dialog.show()
        }
    }
}