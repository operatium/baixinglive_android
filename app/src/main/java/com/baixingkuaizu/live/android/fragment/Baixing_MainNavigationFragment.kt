package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.databinding.BaixingMainNavigationFragmentBinding

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 主导航Fragment，包含底部四个导航按钮：直播、消息、关注、我的
 */
class Baixing_MainNavigationFragment : Baixing_BaseFragment() {
    
    private lateinit var mBaixing_binding: BaixingMainNavigationFragmentBinding
    private var mBaixing_currentTabIndex = -1
    private val mBaixing_fragments = arrayOfNulls<Fragment>(4)
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_binding = BaixingMainNavigationFragmentBinding.inflate(inflater, container, false)
        return mBaixing_binding?.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baixing_initBottomNavigation()
        baixing_switchTab(0) // 默认选中第一个标签
    }
    
    private fun baixing_getFragment(index: Int): Fragment {
        if (mBaixing_fragments[index] == null) {
            when (index) {
                0 -> mBaixing_fragments[index] = Baixing_LiveFragment()
                1 -> mBaixing_fragments[index] = Baixing_MessageFragment()
                2 -> mBaixing_fragments[index] = Baixing_FollowFragment()
                3 -> mBaixing_fragments[index] = Baixing_ProfileFragment()
            }
        }
        return mBaixing_fragments[index]!!
    }
    
    private fun baixing_initBottomNavigation() {
        mBaixing_binding.apply {
            // 设置底部导航按钮点击事件
            baixingTabLive.setOnClickListener { baixing_switchTab(0) }
            baixingTabMessage.setOnClickListener { baixing_switchTab(1) }
            baixingTabFollow.setOnClickListener { baixing_switchTab(2) }
            baixingTabProfile.setOnClickListener { baixing_switchTab(3) }
        }
    }
    
    private fun baixing_switchTab(index: Int) {
        if (index == mBaixing_currentTabIndex) {
            return
        }
        mBaixing_currentTabIndex = index

        // 更新UI状态
        baixing_updateTabState(index)
        
        // 切换Fragment
        val transaction = childFragmentManager.beginTransaction()
        
        // 隐藏当前Fragment
        if (mBaixing_fragments[mBaixing_currentTabIndex] != null) {
            transaction.hide(mBaixing_fragments[mBaixing_currentTabIndex]!!)
        }
        
        // 获取或创建目标Fragment
        val targetFragment = baixing_getFragment(index)
        
        // 如果Fragment未添加，则添加
        if (!targetFragment.isAdded) {
            transaction.replace(R.id.baixing_fragment_container, targetFragment)
        }
        
        // 显示目标Fragment
        transaction.show(targetFragment)
        transaction.commitAllowingStateLoss()
        
    }
    
    private fun baixing_updateTabState(selectedIndex: Int) {
        mBaixing_binding.apply {
            // 重置所有Tab为未选中状态
            baixingTabLiveIcon.setImageResource(R.drawable.baixing_tab_live_normal)
            baixingTabLiveText.setTextColor(resources.getColor(R.color.baixing_tab_normal_color, null))
            
            baixingTabMessageIcon.setImageResource(R.drawable.baixing_tab_message_normal)
            baixingTabMessageText.setTextColor(resources.getColor(R.color.baixing_tab_normal_color, null))
            
            baixingTabFollowIcon.setImageResource(R.drawable.baixing_tab_follow_normal)
            baixingTabFollowText.setTextColor(resources.getColor(R.color.baixing_tab_normal_color, null))
            
            baixingTabProfileIcon.setImageResource(R.drawable.baixing_tab_profile_normal)
            baixingTabProfileText.setTextColor(resources.getColor(R.color.baixing_tab_normal_color, null))
            
            // 设置选中的Tab
            when (selectedIndex) {
                0 -> {
                    baixingTabLiveIcon.setImageResource(R.drawable.baixing_tab_live_selected)
                    baixingTabLiveText.setTextColor(resources.getColor(R.color.baixing_tab_selected_color, null))
                }
                1 -> {
                    baixingTabMessageIcon.setImageResource(R.drawable.baixing_tab_message_selected)
                    baixingTabMessageText.setTextColor(resources.getColor(R.color.baixing_tab_selected_color, null))
                }
                2 -> {
                    baixingTabFollowIcon.setImageResource(R.drawable.baixing_tab_follow_selected)
                    baixingTabFollowText.setTextColor(resources.getColor(R.color.baixing_tab_selected_color, null))
                }
                3 -> {
                    baixingTabProfileIcon.setImageResource(R.drawable.baixing_tab_profile_selected)
                    baixingTabProfileText.setTextColor(resources.getColor(R.color.baixing_tab_selected_color, null))
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
    }
}