package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.baixingkuaizu.live.android.activity.Baixing_MainActivity
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_CategoryDataEntity
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveViewPagerAdapter
import com.baixingkuaizu.live.android.busiess.proxy.Baixing_FragmentProxy
import com.baixingkuaizu.live.android.databinding.BaixingLiveFragmentBinding
import com.baixingkuaizu.live.android.os.Baixing_NetViewState
import com.baixingkuaizu.live.android.viewmodel.Baixing_LiveTableViewModel
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
    private var mBaixing_NetViewState: Baixing_NetViewState? = null

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
        mBaixing_viewModel = ViewModelProvider(this)[Baixing_LiveTableViewModel::class.java]
        baixing_initViewModel()
        baixing_observe()
        mBaixing_viewModel.requestTable()
    }

    private fun baixing_observe() {
        mBaixing_viewModel.apply {
            netError.observe(viewLifecycleOwner) { errorMsg ->
                if (errorMsg.isNullOrEmpty()) return@observe
                mBaixing_binding.baixingLiveErrorLayout.isVisible = true
                CenterToast.show(activity, errorMsg)
            }
            netTable.observe(viewLifecycleOwner) { categoryList ->
                mBaixing_categoryList.clear()
                mBaixing_categoryList.addAll(categoryList)
                mBaixing_binding.baixingLiveContentLayout.isVisible = true
                baixing_setupViewPager()
            }
            netEmpty.observe(viewLifecycleOwner) {
                mBaixing_binding.baixingLiveEmptyLayout.isVisible = it
            }
            viewLoading.observe(viewLifecycleOwner) {
                mBaixing_binding.baixingLoading.isVisible = it
                mBaixing_NetViewState?.init()
            }
        }
    }

    private fun baixing_initViewModel() {
        mBaixing_binding.apply {
            mBaixing_NetViewState = Baixing_NetViewState(
                baixingLiveContentLayout,
                baixingLiveEmptyLayout,
                baixingLiveErrorLayout,
                {
                    baixingLoading.visibility =
                        if (!baixingLiveContentLayout.isVisible && !baixingLiveEmptyLayout.isVisible && !baixingLiveErrorLayout.isVisible) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                }
            ).addListener(this@Baixing_LiveFragment)
            baixingTopBackground.setOnClickListener {
                if (activity is Baixing_MainActivity) {
                    (activity as Baixing_MainActivity).baixing_toSearchFragment()
                }
            }
            baixingLiveErrorRetryBtn.setOnClickListener {
                CenterToast.show(activity, "正在重新加载...")
                mBaixing_viewModel.requestTable()
            }
        }
    }
    
    private fun baixing_setupViewPager() {
        mBaixing_binding.apply {
            if (mBaixing_adapter == null) {
                Baixing_LiveViewPagerAdapter(mBaixing_categoryList, this@Baixing_LiveFragment).let {
                    mBaixing_adapter = it
                    baixingLiveViewPager.adapter = it
                }
                TabLayoutMediator(baixingLiveTabLayout, baixingLiveViewPager) { tab, position ->
                    tab.text = mBaixing_categoryList[position].name
                }.attach()
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: ")
        mBaixing_Proxy.unbind()
        mBaixing_adapter = null
        mBaixing_NetViewState = null
    }
}