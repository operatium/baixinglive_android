package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.followfragment.Baixing_FollowAdapter
import com.baixingkuaizu.live.android.databinding.BaixingFollowFragmentBinding
import com.baixingkuaizu.live.android.viewmodel.Baixing_FollowViewModel
import com.baixingkuaizu.live.android.widget.toast.CenterToast

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 关注页面Fragment
 */
class Baixing_FollowFragment : Baixing_BaseFragment() {
    
    private lateinit var mBaixing_binding: BaixingFollowFragmentBinding
    private lateinit var mBaixing_viewModel: Baixing_FollowViewModel
    private lateinit var mBaixing_onlineAdapter: Baixing_FollowAdapter
    private lateinit var mBaixing_offlineAdapter: Baixing_FollowAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBaixing_binding = BaixingFollowFragmentBinding.inflate(inflater, container, false)
        return mBaixing_binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baixing_initViewModel()
        baixing_initAdapter()
        baixing_initView()
        baixing_observeData()
        baixing_requestData()
    }
    
    private fun baixing_initViewModel() {
        mBaixing_viewModel = ViewModelProvider(this).get(Baixing_FollowViewModel::class.java)
    }
    
    private fun baixing_initAdapter() {
        mBaixing_onlineAdapter = Baixing_FollowAdapter()
        mBaixing_offlineAdapter = Baixing_FollowAdapter()
    }
    
    private fun baixing_initView() {
        mBaixing_binding.apply {
            baixingRvOnline.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mBaixing_onlineAdapter
            }
            
            baixingRvOffline.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mBaixing_offlineAdapter
            }
            
            baixingRefreshLayout.setOnRefreshListener {
                baixing_requestData()
            }
            
            baixingBtnRetry.setOnClickListener {
                mBaixing_viewModel.baixing_retry()
            }
        }
    }
    
    private fun baixing_observeData() {
        mBaixing_viewModel.mBaixing_onlineList.observe(viewLifecycleOwner) { list ->
            mBaixing_onlineAdapter.submitList(list)
            mBaixing_binding.baixingTvOnlineEmpty.isVisible = list.isEmpty()
        }
        
        mBaixing_viewModel.mBaixing_offlineList.observe(viewLifecycleOwner) { list ->
            mBaixing_offlineAdapter.submitList(list)
            mBaixing_binding.baixingTvOfflineEmpty.isVisible = list.isEmpty()
        }
        
        mBaixing_viewModel.mBaixing_isLoading.observe(viewLifecycleOwner) { isLoading ->
            mBaixing_binding.baixingRefreshLayout.isRefreshing = isLoading
            mBaixing_binding.baixingProgressBar.isVisible = isLoading
        }
        
        mBaixing_viewModel.mBaixing_error.observe(viewLifecycleOwner) { error ->
            error?.let {
                CenterToast.show(activity, it)
                mBaixing_binding.baixingErrorGroup.isVisible = true
            } ?: run {
                mBaixing_binding.baixingErrorGroup.isVisible = false
            }
        }
    }
    
    private fun baixing_requestData() {
        mBaixing_viewModel.baixing_requestFollowList()
    }
}