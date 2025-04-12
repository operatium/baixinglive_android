package com.baixingkuaizu.live.android.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.baixingkuaizu.live.android.adatperandroid.AdapterHelper.setClick
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.databinding.BaixingPasswordVerificationDialogBinding
import com.baixingkuaizu.live.android.widget.toast.CenterToast

/**
 * @author yuyuexing
 * @date: 2025/4/16
 * @description: 青少年模式使用时间延长对话框，用于验证监护密码以继续使用
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

        // 初始化视图
        baixing_initViews()
        // 设置监听器
        baixing_setupListeners()
        // 设置对话框属性
        baixing_setupDialogProperties()
    }

    /**
     * 初始化视图
     */
    private fun baixing_initViews() {
        // 设置标题和消息
        mBaixing_binding.baixingDialogTitle.text = mBaixing_title
        mBaixing_binding.baixingDialogMessage.text = mBaixing_message
    }

    /**
     * 设置监听器
     */
    private fun baixing_setupListeners() {
        // 确认按钮点击事件
        mBaixing_binding.baixingConfirmButton.setClick {
            val password = mBaixing_binding.baixingPasswordInput.text.toString()
            
            if (password.isEmpty()) {
                CenterToast.show(context as? Activity, "密码不能为空")
                return@setClick
            }
            
            if (password == mBaixing_localDataManager.baixing_getParentPassword()) {
                // 密码验证成功，记录验证时间
                mBaixing_localDataManager.baixing_setLastVerifiedTime(System.currentTimeMillis())
                
                // 回调验证成功
                mBaixing_onPasswordVerified.invoke()
                
                // 关闭对话框
                dismiss()
                
                CenterToast.show(context as? Activity, "验证成功，已重置使用时间")
            } else {
                CenterToast.show(context as? Activity, "密码错误")
            }
        }
        
        // 取消按钮点击事件 - 在这个场景中禁用取消按钮
        mBaixing_binding.baixingCancelButton.visibility = android.view.View.GONE
    }

    /**
     * 设置对话框属性
     */
    private fun baixing_setupDialogProperties() {
        // 设置是否可以通过返回键或点击外部关闭
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }
}