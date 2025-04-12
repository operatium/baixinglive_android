package com.baixingkuaizu.live.android.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager

/**
 * @author yuyuexing
 * @date: 2025/4/15
 * @description: 退出确认对话框，用于验证密码
 */
class Baixing_ExitDialog(
    context: Context,
    private val mBaixing_localDataManager: Baixing_LocalDataManager,
    private val mBaixing_onExitConfirmed: () -> Unit
) : Dialog(context) {

    private lateinit var mBaixing_passwordInput: EditText
    private lateinit var mBaixing_confirmButton: TextView
    private lateinit var mBaixing_cancelButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.baixing_exit_dialog)
        
        baixing_initViews()
        baixing_setupListeners()
    }
    
    private fun baixing_initViews() {
        mBaixing_passwordInput = findViewById(R.id.baixing_password_input)
        mBaixing_confirmButton = findViewById(R.id.baixing_confirm_button)
        mBaixing_cancelButton = findViewById(R.id.baixing_cancel_button)
    }
    
    private fun baixing_setupListeners() {
        mBaixing_confirmButton.setOnClickListener {
            val password = mBaixing_passwordInput.text.toString()
            
            if (password.isEmpty()) {
                Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (password == mBaixing_localDataManager.baixing_getParentPassword()) {
                dismiss()
                // 执行退出操作
                mBaixing_onExitConfirmed.invoke()
            } else {
                Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show()
            }
        }
        
        mBaixing_cancelButton.setOnClickListener {
            dismiss()
        }
    }
    
    companion object {
        /**
         * 处理退出逻辑
         * 如果青少年模式已启用，则需要密码验证
         * 否则直接退出
         * @param context 上下文
         * @param exitAction 退出操作的回调
         */
        fun baixing_handleExit(context: Context, exitAction: () -> Unit) {
            val localDataManager = Baixing_LocalDataManager.baixing_getInstance(context)
            
            if (localDataManager.baixing_isTeenModeEnabled()) {
                Baixing_ExitDialog(context, localDataManager, exitAction).show()
            } else {
                exitAction.invoke()
            }
        }
    }
} 