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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_Binding = BaixingSelectLoginFragmentBinding.inflate(inflater)
        mBaixing_Binding.baixingPhone.setOnClickListener {
            baixing_goLogin()
        }
        mBaixing_Binding.baixingMore.setOnClickListener {
            baixing_goLogin()
        }
        return mBaixing_Binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baixing_checkLoginStatus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
    
    private fun baixing_checkLoginStatus() {
        if (Baixing_LocalDataManager.getInstance().baixing_isTokenValid()) {
            Baixing_GoRouter.baixing_jumpHomeActivity()
            activity?.finish()
        }
    }
    
    private fun baixing_goLogin() {
        Baixing_GoRouter.baixing_jumpLoginActivity()
        activity?.finish()
    }
}
