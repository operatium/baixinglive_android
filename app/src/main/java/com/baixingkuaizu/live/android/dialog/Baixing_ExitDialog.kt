package com.baixingkuaizu.live.android.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.adatperandroid.AdapterHelper.setClick
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.widget.toast.CenterToast

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
        mBaixing_confirmButton.setClick {
            val password = mBaixing_passwordInput.text.toString()
            
            if (password.isEmpty()) {
                CenterToast.show(context as? Activity,  "密码不能为空")
                return@setClick
            }
            
            if (password == mBaixing_localDataManager.baixing_getParentPassword()) {
                dismiss()
                Baixing_LocalDataManager.getInstance().run {
                    baixing_setTeenModeEnabled(false)
                    baixing_setParentPassword("")
                    CenterToast.show(context as? Activity, "已退出青少年模式")
                }
                mBaixing_onExitConfirmed.invoke()
            } else {
                CenterToast.show(context as? Activity,  "密码错误")
            }
        }
        
        mBaixing_cancelButton.setClick {
            dismiss()
        }
    }
}