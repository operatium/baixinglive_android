package com.baixingkuaizu.live.android.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager

/**
 * @author yuyuexing
 * @date: 2025/4/12
 * @description: 青少年模式设置页面
 */
class Baixing_TeenModeFragment : Baixing_BaseFragment() {
    
    private lateinit var mBaixing_enableButton: Button
    private lateinit var mBaixing_setPasswordText: TextView
    private lateinit var mBaixing_backButton: View
    private lateinit var mBaixing_localDataManager: Baixing_LocalDataManager
    private var mBaixing_parentPassword: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.baixing_teen_mode_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        mBaixing_localDataManager = Baixing_LocalDataManager.baixing_getInstance(requireContext())
        
        // 初始化视图
        baixing_initViews(view)
        // 设置监听器
        baixing_setupListeners()
        // 检查是否已设置密码
        baixing_checkExistingPassword()
    }
    
    private fun baixing_initViews(view: View) {
        mBaixing_enableButton = view.findViewById(R.id.baixing_enable_teen_mode)
        mBaixing_setPasswordText = view.findViewById(R.id.baixing_set_password)
        mBaixing_backButton = view.findViewById(R.id.baixing_back)
    }
    
    private fun baixing_setupListeners() {
        // 返回按钮点击事件
        mBaixing_backButton.setClick {
            requireActivity().finish()
        }
        
        // 开启青少年模式按钮点击事件
        mBaixing_enableButton.setClick {
            // 每次都需要设置密码
            baixing_showSetPasswordDialog()
        }
    }
    
    /**
     * 检查是否已经设置过监护密码
     */
    private fun baixing_checkExistingPassword() {
        val savedPassword = mBaixing_localDataManager.baixing_getParentPassword()
        if (savedPassword.isNotEmpty()) {
            mBaixing_parentPassword = savedPassword
            mBaixing_setPasswordText.text = "每次开启青少年模式都需要设置密码"
            
            // 如果已经启用了青少年模式，更新按钮文字
            if (mBaixing_localDataManager.baixing_isTeenModeEnabled()) {
                mBaixing_enableButton.text = "重新设置密码并进入"
            } else {
                mBaixing_enableButton.text = "设置密码并进入"
            }
        }
    }
    
    /**
     * 显示设置监护密码对话框
     */
    private fun baixing_showSetPasswordDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.baixing_set_password_dialog, null)
        val passwordInput = dialogView.findViewById<EditText>(R.id.baixing_password_input)
        val confirmInput = dialogView.findViewById<EditText>(R.id.baixing_confirm_password_input)
        
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("设置监护密码")
            .setView(dialogView)
            .setPositiveButton("确定") { _, _ ->
                val password = passwordInput.text.toString()
                val confirmPassword = confirmInput.text.toString()
                
                if (password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(requireContext(), "密码不能为空", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                
                if (password != confirmPassword) {
                    Toast.makeText(requireContext(), "两次输入的密码不一致", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                
                mBaixing_parentPassword = password
                // 保存密码到本地存储
                baixing_saveParentPassword(password)
                
                // 启用青少年模式
                baixing_enableTeenMode()
            }
            .setNegativeButton("取消", null)
            .create()
        
        dialog.show()
    }
    
    /**
     * 保存监护密码
     */
    private fun baixing_saveParentPassword(password: String) {
        // 使用LocalDataManager保存密码
        mBaixing_localDataManager.baixing_setParentPassword(password)
        mBaixing_setPasswordText.text = "已设置监护密码"
    }
    
    /**
     * 启用青少年模式
     */
    private fun baixing_enableTeenMode() {
        // 保存青少年模式状态
        mBaixing_localDataManager.baixing_setTeenModeEnabled(true)
        
        Toast.makeText(requireContext(), "青少年模式已启用", Toast.LENGTH_SHORT).show()
        
        // 跳转到播放列表页面
        baixing_navigateToPlayList()
    }
    
    /**
     * 跳转到播放列表页面
     */
    private fun baixing_navigateToPlayList() {
        val playListFragment = Baixing_PlayListFragment.newInstance()
        
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.baixing_teen_mode_container, playListFragment)
            .commit()
    }
    
    companion object {
        fun newInstance(): Baixing_TeenModeFragment {
            return Baixing_TeenModeFragment()
        }
    }
} 