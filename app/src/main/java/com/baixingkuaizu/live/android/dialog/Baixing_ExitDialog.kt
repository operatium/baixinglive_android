package com.baixingkuaizu.live.android.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.baixingkuaizu.live.android.adatperandroid.AdapterHelper.setClick
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.databinding.BaixingExitDialogBinding
import com.baixingkuaizu.live.android.widget.toast.CenterToast

/**
 * @author yuyuexing
 * @date: 2025/4/15
 * @description: 退出确认对话框，用于验证密码以退出青少年模式
 */
class Baixing_ExitDialog(
    context: Context,
    private val mBaixing_localDataManager: Baixing_LocalDataManager,
    private val mBaixing_onExitConfirmed: () -> Unit
) : Dialog(context) {

    private val mBaixing_binding: BaixingExitDialogBinding by lazy {
        BaixingExitDialogBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(mBaixing_binding.root)
        
        baixing_setupListeners()
    }
    
    private fun baixing_setupListeners() {
        mBaixing_binding.baixingConfirmButton.setClick {
            val password = mBaixing_binding.baixingPasswordInput.text.toString()
            
            if (password.isEmpty()) {
                CenterToast.show(context as? Activity, "密码不能为空")
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
                CenterToast.show(context as? Activity, "密码错误")
            }
        }
        
        mBaixing_binding.baixingCancelButton.setClick {
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
            val localDataManager = Baixing_LocalDataManager.getInstance()
            
            if (localDataManager.baixing_isTeenModeEnabled()) {
                Baixing_ExitDialog(context, localDataManager, exitAction).show()
            } else {
                exitAction.invoke()
            }
        }
    }
}