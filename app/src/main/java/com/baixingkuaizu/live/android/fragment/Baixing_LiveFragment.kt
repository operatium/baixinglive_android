package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_CategoryDataEntity
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveViewPagerAdapter
import com.baixingkuaizu.live.android.busiess.proxy.Baixing_FragmentProxy
import com.baixingkuaizu.live.android.databinding.BaixingLiveFragmentBinding
import com.baixingkuaizu.live.android.os.Baixing_NetViewState
import com.baixingkuaizu.live.android.os.Baixing_ViewVisibilityListener
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
        baixing_initViewModel()
    }
    
    private fun baixing_initViewModel() {

        // 替换三个重复的ViewVisibilityListener为统一的方法
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
        }

        mBaixing_viewModel = ViewModelProvider(this)[Baixing_LiveTableViewModel::class.java]
        
        // 观察网络错误
        mBaixing_viewModel.netError.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg.isNullOrEmpty()) return@observe
            baixing_showErrorView()
        }
        
        // 观察栏目数据
        mBaixing_viewModel.netTable.observe(viewLifecycleOwner) { categoryList ->
            if (categoryList.isEmpty()) {
                baixing_showEmptyView()
            } else {
                mBaixing_categoryList.clear()
                mBaixing_categoryList.addAll(categoryList)
                baixing_showContentView()
            }
        }
        
        // 请求栏目数据
        mBaixing_viewModel.requestTable()
    }

    /**
     * 根据各视图的可见状态更新加载指示器的显示
     */
    private fun baixing_updateLoadingVisibility(
        contentVisible: Boolean = mBaixing_binding.baixingLiveContentLayout.isVisible,
        emptyVisible: Boolean = mBaixing_binding.baixingLiveEmptyLayout.isVisible,
        errorVisible: Boolean = mBaixing_binding.baixingLiveErrorLayout.isVisible
    ) {
        mBaixing_binding.apply {
            // 当任何一个内容视图显示时，隐藏其他视图
            if (contentVisible) {
                baixingLiveErrorLayout.visibility = View.GONE
                baixingLiveEmptyLayout.visibility = View.GONE
            } else if (emptyVisible) {
                baixingLiveErrorLayout.visibility = View.GONE
                baixingLiveContentLayout.visibility = View.GONE
            } else if (errorVisible) {
                baixingLiveContentLayout.visibility = View.GONE
                baixingLiveEmptyLayout.visibility = View.GONE
            }


        }
    }
    
    /**
     * 显示内容视图
     */
    private fun baixing_showContentView() {
        baixing_updateViewVisibility(showContent = true)
        baixing_setupViewPager()
    }
    
    /**
     * 设置ViewPager和TabLayout
     */
    private fun baixing_setupViewPager() {
        mBaixing_binding.apply {
            if (mBaixing_adapter == null) {
                baixingLiveViewPager.adapter =
                    Baixing_LiveViewPagerAdapter(mBaixing_categoryList, this@Baixing_LiveFragment).apply {
                        mBaixing_adapter = this
                    }
                
                // 设置TabLayout与ViewPager的联动
                TabLayoutMediator(baixingLiveTabLayout, baixingLiveViewPager) { tab, position ->
                    tab.text = mBaixing_categoryList[position].name
                }.attach()
            }
        }
    }
    
    /**
     * 显示错误视图
     */
    private fun baixing_showErrorView() {
        baixing_updateViewVisibility(showError = true)
        mBaixing_binding.baixingLiveErrorRetryBtn.setOnClickListener {
            CenterToast.show(activity, "正在重新加载...")
            mBaixing_viewModel.requestTable()
        }
    }
    
    /**
     * 显示空数据视图
     */
    private fun baixing_showEmptyView() {
        baixing_updateViewVisibility(showEmpty = true)
    }
    
    /**
     * 更新各视图的可见性
     */
    private fun baixing_updateViewVisibility(
        showContent: Boolean = false,
        showError: Boolean = false,
        showEmpty: Boolean = false
    ) {
        mBaixing_binding.apply {
            baixingLiveContentLayout.visibility = if (showContent) View.VISIBLE else View.GONE
            baixingLiveErrorLayout.visibility = if (showError) View.VISIBLE else View.GONE
            baixingLiveEmptyLayout.visibility = if (showEmpty) View.VISIBLE else View.GONE
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