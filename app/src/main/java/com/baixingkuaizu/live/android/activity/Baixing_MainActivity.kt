package com.baixingkuaizu.live.android.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.dialog.Baixing_TeenModeDialog

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 主活动页面
 */
class Baixing_MainActivity : Baixing_BaseActivity() {
    
    private lateinit var mBaixing_localDataManager: Baixing_LocalDataManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
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
                // 处理进入青少年模式的逻辑
                // 这里可以添加实际的青少年模式实现
                mBaixing_localDataManager.baixing_setTeenModeDialogShown(true)
            }
            dialog.baixing_setOnDismissListener {
                // 用户点击"我知道了"按钮
                mBaixing_localDataManager.baixing_setTeenModeDialogShown(true)
            }
            dialog.setCancelable(false) // 防止用户通过返回键关闭对话框
            dialog.show()
        }
    }
}