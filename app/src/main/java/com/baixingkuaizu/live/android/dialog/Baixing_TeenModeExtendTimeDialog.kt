package com.baixingkuaizu.live.android.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.baixingkuaizu.live.android.adatperandroid.Baixing_AdapterHelper.setClick
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.databinding.BaixingPasswordVerificationDialogBinding
import com.baixingkuaizu.live.android.widget.toast.CenterToast

/**
 * @author yuyuexing
 * @date: 2025/4/16
 * @description: 青少年模式使用时间延长对话框，用于验证监护密码以继续使用。当用户使用时间达到上限（40分钟）时弹出，要求输入正确的监护密码才能继续使用。禁用了取消功能，确保青少年保护机制的严格执行。与LocalDataManager协同工作，负责密码验证和时间记录。使用ViewBinding进行视图绑定，通过回调机制通知调用方验证结果。
 */
class Baixing_TeenModeExtendTimeDialog(
    context: Context,
    private val mBaixing_onPasswordVerified: () -> Unit
) : Dialog(context) {
    private val mBaixing_binding: BaixingPasswordVerificationDialogBinding by lazy {
        BaixingPasswordVerificationDialogBinding.inflate(LayoutInflater.from(context))
    }
    private val mBaixing_localDataManager: Baixing_LocalDataManager = Baixing_LocalDataManager.getInstance()
    private val mBaixing_title: String = "使用时间已达上限"
    private val mBaixing_message: String = "您今日的使用时间已达到上限（40分钟），请输入监护密码继续使用。"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(mBaixing_binding.root)

        baixing_initViews()
        baixing_setupListeners()
        baixing_setupDialogProperties()
    }

    private fun baixing_initViews() {
        mBaixing_binding.baixingDialogTitle.text = mBaixing_title
        mBaixing_binding.baixingDialogMessage.text = mBaixing_message
    }

    private fun baixing_setupListeners() {
        mBaixing_binding.baixingConfirmButton.setClick {
            val password = mBaixing_binding.baixingPasswordInput.text.toString()
            
            if (password.isEmpty()) {
                CenterToast.show(context as? Activity, "密码不能为空")
                return@setClick
            }
            
            if (password == mBaixing_localDataManager.baixing_getParentPassword()) {
                mBaixing_localDataManager.baixing_setLastVerifiedTime(System.currentTimeMillis())
                
                mBaixing_onPasswordVerified.invoke()
                
                dismiss()
                
                CenterToast.show(context as? Activity, "验证成功，已重置使用时间")
            } else {
                CenterToast.show(context as? Activity, "密码错误")
            }
        }
        
        mBaixing_binding.baixingCancelButton.visibility = android.view.View.GONE
    }

    private fun baixing_setupDialogProperties() {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }
}