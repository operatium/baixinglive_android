package com.baixingkuaizu.live.android.activity

import android.os.Bundle
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.busiess.router.Baixing_GoRouter
import com.baixingkuaizu.live.android.databinding.BaixingMainActivityBinding
import com.baixingkuaizu.live.android.dialog.Baixing_TeenModeDialog
import com.baixingkuaizu.live.android.fragment.Baixing_FollowFragment
import com.baixingkuaizu.live.android.fragment.Baixing_LiveFragment
import com.baixingkuaizu.live.android.fragment.Baixing_MessageFragment
import com.baixingkuaizu.live.android.fragment.Baixing_ProfileFragment

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 主活动页面，应用的核心页面，负责应用的主要功能展示并管理青少年模式状态。使用ViewBinding进行视图绑定，在启动时会检查青少年模式状态，未启用则显示提示对话框，已启用则跳转到青少年模式页面。
 */
class Baixing_MainActivity : Baixing_BaseActivity() {
    private val mBaixing_localDataManager = Baixing_LocalDataManager.getInstance()

    private lateinit var mBaixing_binding: BaixingMainActivityBinding

    private var mBaixing_currentTabIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaixing_binding = BaixingMainActivityBinding.inflate(layoutInflater)
        setContentView(mBaixing_binding.root)
        mBaixing_binding.root.setWindowListener()
        
        if (!mBaixing_localDataManager.baixing_isTeenModeEnabled()) {
            baixing_showTeenModeDialogIfNeeded()
        } else {
            Baixing_GoRouter.baixing_jumpTeenModeActivity()
            return
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.baixing_fragment_container, Baixing_LiveFragment())
            commit()
        }
        baixing_updateTabState(0)
        baixing_initBottomNavigation()
    }
    
    private fun baixing_showTeenModeDialogIfNeeded() {
        if (!mBaixing_localDataManager.baixing_isTeenModeDialogShown()) {
            val dialog = Baixing_TeenModeDialog(this)
            dialog.baixing_setOnEnterTeenModeListener {
                Baixing_GoRouter.baixing_jumpTeenModeActivity()
            }
            dialog.baixing_setOnDismissListener {
                mBaixing_localDataManager.baixing_setTeenModeDialogShown()
            }
            dialog.show()
        }
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

        supportFragmentManager.beginTransaction().apply {
            val f = when(index) {
                0 -> Baixing_LiveFragment()
                1 -> Baixing_MessageFragment()
                2 -> Baixing_FollowFragment()
                3 -> Baixing_ProfileFragment()
                else -> Baixing_LiveFragment()
            }
            replace(R.id.baixing_fragment_container, f)
            commit()
        }

    }
}