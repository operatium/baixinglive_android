package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_CategoryData
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
    
    private lateinit var mBaixing_binding: BaixingLiveFragmentBinding
    private val mBaixing_categoryList = mutableListOf<Baixing_CategoryData>()
    private lateinit var mBaixing_viewModel: Baixing_LiveTableViewModel
    private var mBaixing_loading: Baixing_FullScreenLoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_binding = BaixingLiveFragmentBinding.inflate(inflater, container, false)
        return mBaixing_binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baixing_initViewModel()
        mBaixing_loading = Baixing_FullScreenLoadingDialog(context?:return)
        mBaixing_loading?.show()
    }
    
    private fun baixing_initViewModel() {
        mBaixing_viewModel = ViewModelProvider(this)[Baixing_LiveTableViewModel::class.java].apply {
            netError.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) return@observe
                baixing_retry()
                mBaixing_loading?.hide()
            }
            netTable.observe(viewLifecycleOwner) {
                mBaixing_loading?.hide()
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
    
    /**
     * 显示内容视图
     */
    private fun baixing_showContent() {
        mBaixing_binding.apply {
            // 隐藏错误和空视图
            baixingLiveErrorLayout.visibility = View.GONE
            baixingLiveEmptyLayout.visibility = View.GONE
            
            // 显示内容视图
            baixingLiveContentLayout.visibility = View.VISIBLE
            
            // 设置ViewPager2
            baixingLiveViewPager.adapter = object : FragmentStateAdapter(this@Baixing_LiveFragment) {
                override fun getItemCount(): Int = mBaixing_categoryList.size

                override fun createFragment(position: Int): Fragment {
                    val categoryId = mBaixing_categoryList[position].id
                    return Baixing_LiveCategoryFragment.baixing_newInstance(categoryId)
                }
            }

            // 设置TabLayout与ViewPager2联动
            TabLayoutMediator(baixingLiveTabLayout, baixingLiveViewPager) { tab, position ->
                tab.text = mBaixing_categoryList[position].name
            }.attach()
        }
    }
    
    /**
     * 显示网络错误视图并提供重试功能
     */
    private fun baixing_retry() {
        mBaixing_binding.apply {
            // 隐藏内容和空视图
            baixingLiveContentLayout.visibility = View.GONE
            baixingLiveEmptyLayout.visibility = View.GONE
            
            // 显示错误视图
            baixingLiveErrorLayout.visibility = View.VISIBLE
            
            // 设置重试按钮点击事件
            baixingLiveErrorRetryBtn.setOnClickListener {
                // 显示加载中提示
                CenterToast.show(activity, "正在重新加载...")
                
                // 重新请求数据
                mBaixing_viewModel.requestTable()
            }
        }
    }
    
    /**
     * 显示空数据视图
     */
    private fun baixing_empty() {
        mBaixing_binding.apply {
            // 隐藏内容和错误视图
            baixingLiveContentLayout.visibility = View.GONE
            baixingLiveErrorLayout.visibility = View.GONE
            
            // 显示空视图
            baixingLiveEmptyLayout.visibility = View.VISIBLE
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        mBaixing_loading?.dismiss()
    }
}