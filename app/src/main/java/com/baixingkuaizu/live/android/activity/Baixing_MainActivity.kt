package com.baixingkuaizu.live.android.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.busiess.router.Baixing_GoRouter
import com.baixingkuaizu.live.android.databinding.BaixingMainActivityBinding
import com.baixingkuaizu.live.android.dialog.Baixing_TeenModeDialog
import com.baixingkuaizu.live.android.fragment.Baixing_FollowFragment
import com.baixingkuaizu.live.android.fragment.Baixing_LiveFragment
import com.baixingkuaizu.live.android.fragment.Baixing_MessageFragment
import com.baixingkuaizu.live.android.fragment.Baixing_ProfileFragment
import com.baixingkuaizu.live.android.fragment.Baixing_SearchFragment

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 主活动页面，应用的核心页面，负责应用的主要功能展示并管理青少年模式状态。使用ViewBinding进行视图绑定，在启动时会检查青少年模式状态，未启用则显示提示对话框，已启用则跳转到青少年模式页面。
 */
class Baixing_MainActivity : Baixing_BaseActivity() {
    private val mBaixing_localDataManager = Baixing_LocalDataManager.getInstance()

    private lateinit var mBaixing_binding: BaixingMainActivityBinding

    private var mBaixing_currentTabIndex = -1
    private val mBaixing_fragments = arrayOfNulls<Baixing_BaseFragment>(4)
    
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

        baixing_initFragments()
        baixing_updateTabState(0)
        baixing_initBottomNavigation()
        baixing_switchTab(0)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                supportFragmentManager.run {
                    if (backStackEntryCount > 0) {
                        popBackStack()
                    } else {
                        moveTaskToBack(true)
                    }
                }
            }
        })
    }
    
    private fun baixing_initFragments() {
        mBaixing_fragments[0] = Baixing_LiveFragment()
        mBaixing_fragments[1] = Baixing_MessageFragment()
        mBaixing_fragments[2] = Baixing_FollowFragment()
        mBaixing_fragments[3] = Baixing_ProfileFragment()
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
            baixingTabLiveIcon.setImageResource(R.drawable.baixing_tab_live_normal)
            baixingTabLiveText.setTextColor(resources.getColor(R.color.baixing_tab_normal_color, null))

            baixingTabMessageIcon.setImageResource(R.drawable.baixing_tab_message_normal)
            baixingTabMessageText.setTextColor(resources.getColor(R.color.baixing_tab_normal_color, null))

            baixingTabFollowIcon.setImageResource(R.drawable.baixing_tab_follow_normal)
            baixingTabFollowText.setTextColor(resources.getColor(R.color.baixing_tab_normal_color, null))

            baixingTabProfileIcon.setImageResource(R.drawable.baixing_tab_profile_normal)
            baixingTabProfileText.setTextColor(resources.getColor(R.color.baixing_tab_normal_color, null))

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

    fun baixing_toSearchFragment() {
        supportFragmentManager.beginTransaction().run {
            add(R.id.baixing_fragment_container, Baixing_SearchFragment())
            addToBackStack(null)
            commit()
        }
    }

    private fun baixing_initBottomNavigation() {
        mBaixing_binding.apply {
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
        
        baixing_updateTabState(index)
        
        val transaction = supportFragmentManager.beginTransaction()
        
        if (mBaixing_currentTabIndex != -1 && mBaixing_fragments[mBaixing_currentTabIndex] != null) {
            transaction.hide(mBaixing_fragments[mBaixing_currentTabIndex]!!)
        }
        
        if (mBaixing_fragments[index]?.isAdded != true) {
            transaction.add(R.id.baixing_fragment_container, mBaixing_fragments[index]!!)
        }
        
        transaction.show(mBaixing_fragments[index]!!)
        transaction.commitAllowingStateLoss()
        
        mBaixing_currentTabIndex = index
    }
}