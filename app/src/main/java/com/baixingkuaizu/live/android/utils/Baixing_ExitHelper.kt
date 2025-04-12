package com.baixingkuaizu.live.android.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager

/**
 * @author yuyuexing
 * @date: 2025/4/15
 * @description: 退出帮助类，处理青少年模式的退出逻辑
 */
class Baixing_ExitHelper(
    private val mBaixing_activity: Activity,
    private val mBaixing_localDataManager: Baixing_LocalDataManager
) {

    /**
     * 处理退出逻辑
     * 如果青少年模式已启用，则需要密码验证
     * 否则直接退出
     * @param exitAction 退出操作的回调
     */
    fun baixing_handleExit(exitAction: () -> Unit) {
        if (mBaixing_localDataManager.baixing_isTeenModeEnabled()) {
            baixing_showPasswordVerificationDialog(exitAction)
        } else {
            exitAction.invoke()
        }
    }

    /**
     * 显示密码验证对话框
     * @param exitAction 验证成功后执行的退出操作
     */
    private fun baixing_showPasswordVerificationDialog(exitAction: () -> Unit) {
        val dialogView = LayoutInflater.from(mBaixing_activity)
            .inflate(R.layout.baixing_password_verification_dialog, null)
        val passwordInput = dialogView.findViewById<EditText>(R.id.baixing_password_input)
        
        val dialog = AlertDialog.Builder(mBaixing_activity)
            .setView(dialogView)
            .setTitle("退出青少年模式")
            .setPositiveButton("确定", null) // 后面手动设置监听器以防止对话框自动关闭
            .setNegativeButton("取消", null)
            .create()
        
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val password = passwordInput.text.toString()
                
                if (password.isEmpty()) {
                    Toast.makeText(mBaixing_activity, "密码不能为空", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                
                if (password == mBaixing_localDataManager.baixing_getParentPassword()) {
                    dialog.dismiss()
                    // 执行退出操作
                    exitAction.invoke()
                } else {
                    Toast.makeText(mBaixing_activity, "密码错误", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        dialog.show()
    }
}