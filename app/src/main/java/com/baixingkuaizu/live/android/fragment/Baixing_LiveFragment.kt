package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_CategoryData
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveListCache
import com.baixingkuaizu.live.android.databinding.BaixingLiveFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播页面Fragment，包含多个栏目的ViewPager
 */
class Baixing_LiveFragment : Baixing_BaseFragment() {
    
    private var mBaixing_binding: BaixingLiveFragmentBinding? = null
    private val mBaixing_categoryList = mutableListOf<Baixing_CategoryData>()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_binding = BaixingLiveFragmentBinding.inflate(inflater, container, false)
        return mBaixing_binding?.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baixing_initData()
        baixing_initView()
    }
    
    private fun baixing_initData() {
        // 获取栏目数据
        mBaixing_categoryList.clear()
        mBaixing_categoryList.addAll(Baixing_LiveListCache.getInstance().baixing_getCategoryList())
    }
    
    private fun baixing_initView() {
        mBaixing_binding?.apply {
            // 设置ViewPager2
            baixingLiveViewPager.adapter = object : FragmentStateAdapter(this@Baixing_LiveFragment) {
                override fun getItemCount(): Int = mBaixing_categoryList.size
                
                override fun createFragment(position: Int): Fragment {
                    val categoryId = mBaixing_categoryList[position].id
                    return Baixing_LiveCategoryFragment.newInstance(categoryId)
                }
            }
            
            // 设置TabLayout与ViewPager2联动
            TabLayoutMediator(baixingLiveTabLayout, baixingLiveViewPager) { tab, position ->
                tab.text = mBaixing_categoryList[position].name
            }.attach()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        mBaixing_binding = null
    }
}