package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.messagefragment.Baixing_MessageAdapter
import com.baixingkuaizu.live.android.busiess.messagefragment.Baixing_MessageDataCache
import com.baixingkuaizu.live.android.databinding.BaixingMessageFragmentBinding
import com.baixingkuaizu.live.android.viewmodel.Baixing_MessageViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 消息页面Fragment
 */
class Baixing_MessageFragment : Baixing_BaseFragment() {
    
    private lateinit var mBaixing_binding: BaixingMessageFragmentBinding
    private lateinit var mBaixing_viewModel: Baixing_MessageViewModel
    private lateinit var mBaixing_adapter: Baixing_MessageAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBaixing_binding = BaixingMessageFragmentBinding.inflate(inflater, container, false)
        return mBaixing_binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        context?.let { Baixing_MessageDataCache.baixing_init(it) }
        
        mBaixing_viewModel = ViewModelProvider(this).get(Baixing_MessageViewModel::class.java)
        
        mBaixing_adapter = Baixing_MessageAdapter()
        
        mBaixing_binding.baixingRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mBaixing_adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                        
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5
                            && firstVisibleItemPosition >= 0) {
                            mBaixing_viewModel.baixing_loadNextPage()
                        }
                    }
                }
            })
        }
        
        mBaixing_binding.baixingRefreshLayout.apply {
            setOnRefreshListener {
                mBaixing_viewModel.baixing_requestMessageUserList(true)
            }
        }
        
        mBaixing_binding.baixingBtnRetry.setOnClickListener {
            mBaixing_viewModel.baixing_retryLastRequest()
        }
        
        lifecycleScope.launch {
            mBaixing_viewModel.mBaixing_messageState.collectLatest { state ->
                mBaixing_binding.baixingProgressBar.isVisible = state.isLoading
                mBaixing_binding.baixingRefreshLayout.isRefreshing = state.isLoading && state.page == 1
                
                if (state.error != null) {
                    mBaixing_binding.baixingErrorGroup.isVisible = true
                    mBaixing_binding.baixingTvError.text = state.error.message ?: "异常"
                } else {
                    mBaixing_binding.baixingErrorGroup.isVisible = false
                }
                
                mBaixing_adapter.submitList(state.items)
                
                if (state.endReached && state.items.isEmpty()) {
                    mBaixing_binding.baixingTvEmpty.isVisible = true
                } else {
                    mBaixing_binding.baixingTvEmpty.isVisible = false
                }
            }
        }
        
        mBaixing_viewModel.baixing_requestMessageUserList(true)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
    }
}