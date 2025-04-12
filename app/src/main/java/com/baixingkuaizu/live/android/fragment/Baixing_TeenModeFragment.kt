package com.baixingkuaizu.live.android.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.activity.Baixing_TeenModeActivity
import com.baixingkuaizu.live.android.adatperandroid.AdapterHelper.setClick
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.databinding.BaixingSetPasswordDialogBinding
import com.baixingkuaizu.live.android.databinding.BaixingTeenModeFragmentBinding
import com.baixingkuaizu.live.android.widget.toast.CenterToast

/**
 * @author yuyuexing
 * @date: 2025/4/12
 * @description: 青少年模式设置页面
 */
class Baixing_TeenModeFragment : Baixing_BaseFragment() {
    
    private lateinit var mBaixing_localDataManager: Baixing_LocalDataManager

    private lateinit var mBaixing_binding: BaixingTeenModeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_binding = BaixingTeenModeFragmentBinding.inflate(inflater)
        return mBaixing_binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBaixing_localDataManager = Baixing_LocalDataManager.getInstance()
        baixing_setupListeners()
        if (mBaixing_localDataManager.baixing_isTeenModeEnabled()) {
            (activity as? Baixing_TeenModeActivity)?.baixing_toTeenPlayListFragment()
        }
    }

    private fun baixing_setupListeners() {
        mBaixing_binding.baixingBack.setClick {
            activity?.finish()
        }
        
        mBaixing_binding.baixingEnableTeenMode.setClick {
            baixing_showSetPasswordDialog()
        }
    }
    
    /**
     * 显示设置监护密码对话框
     */
    private fun baixing_showSetPasswordDialog() {
        val dialogBinding = BaixingSetPasswordDialogBinding.inflate(LayoutInflater.from(context))
        
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("设置监护密码")
            .setView(dialogBinding.root)
            .setPositiveButton("确定") { _, _ ->
                val password = dialogBinding.baixingPasswordInput.text.toString()
                val confirmPassword = dialogBinding.baixingConfirmPasswordInput.text.toString()
                
                if (password.isEmpty() || confirmPassword.isEmpty()) {
                    CenterToast.show(requireActivity(),"密码不能为空")
                    return@setPositiveButton
                }
                
                if (password != confirmPassword) {
                    CenterToast.show(requireActivity(),"两次输入的密码不一致")
                    return@setPositiveButton
                }
                mBaixing_localDataManager.baixing_setParentPassword(password)
                mBaixing_localDataManager.baixing_setTeenModeEnabled(true)
                CenterToast.show(requireActivity(),"青少年模式已启用")
                baixing_navigateToPlayList()
            }
            .setNegativeButton("取消", null)
            .create()
        
        dialog.show()
    }
    
    /**
     * 跳转到播放列表页面
     */
    private fun baixing_navigateToPlayList() {
        val playListFragment = Baixing_TeenPlayListFragment.newInstance()
        
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