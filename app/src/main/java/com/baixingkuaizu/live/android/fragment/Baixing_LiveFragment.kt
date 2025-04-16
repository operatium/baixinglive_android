package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_CategoryDataEntity
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveViewPagerAdapter
import com.baixingkuaizu.live.android.busiess.proxy.Baixing_FragmentProxy
import com.baixingkuaizu.live.android.databinding.BaixingLiveFragmentBinding
import com.baixingkuaizu.live.android.viewmodel.Baixing_LiveTableViewModel
import com.baixingkuaizu.live.android.widget.loading.Baixing_FullScreenLoadingDialog
import com.baixingkuaizu.live.android.widget.toast.CenterToast
import com.google.android.material.tabs.TabLayoutMediator

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播页面Fragment，包含多个栏目的ViewPager
 */
class Baixing_LiveFragment : Baixing_BaseFragment() {
    private val TAG = "yyx类Baixing_LiveFragment"
    
    private lateinit var mBaixing_binding: BaixingLiveFragmentBinding
    private val mBaixing_categoryList = ArrayList<Baixing_CategoryDataEntity>()
    private lateinit var mBaixing_viewModel: Baixing_LiveTableViewModel
    private val mBaixing_Proxy = Baixing_FragmentProxy(this)
    private var mBaixing_adapter: Baixing_LiveViewPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")
        mBaixing_binding = BaixingLiveFragmentBinding.inflate(inflater, container, false)
        return mBaixing_binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBaixing_Proxy.bind(this)
        baixing_initViewModel()
        mBaixing_binding.baixingLoading.visibility = View.VISIBLE
    }
    
    private fun baixing_initViewModel() {
        mBaixing_viewModel = ViewModelProvider(this)[Baixing_LiveTableViewModel::class.java].apply {
            netError.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) return@observe
                baixing_retry()
                mBaixing_binding.baixingLoading.visibility = View.GONE
            }
            netTable.observe(viewLifecycleOwner) {
                mBaixing_binding.baixingLoading.visibility = View.GONE
                if (it.isEmpty()) {
                    baixing_empty()
                } else {
                    mBaixing_categoryList.clear()
                    mBaixing_categoryList.addAll(it)
                    baixing_showContent()
                }
            }
            requestTable()
        }
    }
    
    private fun baixing_showContent() {
        mBaixing_binding.apply {
            baixingLiveErrorLayout.visibility = View.GONE
            baixingLiveEmptyLayout.visibility = View.GONE
            
            baixingLiveContentLayout.visibility = View.VISIBLE

            if (mBaixing_adapter == null) {
                baixingLiveViewPager.adapter =
                    Baixing_LiveViewPagerAdapter(mBaixing_categoryList, this@Baixing_LiveFragment).apply {
                        mBaixing_adapter = this
                    }
            }
            TabLayoutMediator(baixingLiveTabLayout, baixingLiveViewPager) { tab, position ->
                tab.text = mBaixing_categoryList[position].name
            }.attach()
        }
    }
    
    private fun baixing_retry() {
        mBaixing_binding.apply {
            baixingLiveContentLayout.visibility = View.GONE
            baixingLiveEmptyLayout.visibility = View.GONE
            
            baixingLiveErrorLayout.visibility = View.VISIBLE
            
            baixingLiveErrorRetryBtn.setOnClickListener {
                CenterToast.show(activity, "正在重新加载...")
                mBaixing_binding.baixingLoading.visibility = View.VISIBLE
                mBaixing_viewModel.requestTable()
            }
        }
    }
    
    private fun baixing_empty() {
        mBaixing_binding.apply {
            baixingLiveContentLayout.visibility = View.GONE
            baixingLiveErrorLayout.visibility = View.GONE
            
            baixingLiveEmptyLayout.visibility = View.VISIBLE
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: ")
        mBaixing_Proxy.unbind()
    }
}