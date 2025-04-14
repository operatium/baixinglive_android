package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.databinding.BaixingFragmentLiveBinding

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播页面Fragment
 */
class Baixing_LiveFragment : Baixing_BaseFragment() {
    
    private var mBaixing_binding: BaixingFragmentLiveBinding? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_binding = BaixingFragmentLiveBinding.inflate(inflater, container, false)
        return mBaixing_binding?.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baixing_initView()
    }
    
    private fun baixing_initView() {
        // 初始化直播页面
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        mBaixing_binding = null
    }
}