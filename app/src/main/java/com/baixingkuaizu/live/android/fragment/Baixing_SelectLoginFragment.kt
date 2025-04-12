package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.busiess.router.Baixing_GoRouter
import com.baixingkuaizu.live.android.databinding.BaixingSelectLoginFragmentBinding

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 登录选择界面Fragment
 */
class Baixing_SelectLoginFragment : Baixing_BaseFragment() {
    private lateinit var mBaixing_Binding: BaixingSelectLoginFragmentBinding
    private lateinit var mBaixing_localDataManager: Baixing_LocalDataManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_Binding = BaixingSelectLoginFragmentBinding.inflate(inflater)
        mBaixing_Binding.baixingPhone.setOnClickListener {
            goLogin()
        }
        mBaixing_Binding.baixingMore.setOnClickListener {
            goLogin()
        }
        return mBaixing_Binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 初始化本地数据管理器
        mBaixing_localDataManager = Baixing_LocalDataManager.baixing_getInstance(requireContext())
        
        // 检查用户是否已登录且token有效
        baixing_checkLoginStatus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
    
    /**
     * 检查用户是否已登录且token有效
     * 如果已登录且token有效，直接跳转到主页面
     */
    private fun baixing_checkLoginStatus() {
        // 检查token是否有效
        if (mBaixing_localDataManager.baixing_isTokenValid()) {
            // token有效，直接跳转到主页面
            baixing_goToMainActivity()
        }
        // 如果token无效或不存在，不做处理，用户需要手动登录
    }
    
    /**
     * 跳转到主页面
     */
    private fun baixing_goToMainActivity() {
        Baixing_GoRouter.baixing_jumpMainActivity(requireContext())
        requireActivity().finish()
    }

    private fun goLogin() {
        Baixing_GoRouter.baixing_jumpLoginActivity()
        requireActivity().finish()
    }
}
