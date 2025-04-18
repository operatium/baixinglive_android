package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.profilefragment.Baixing_ProfileData
import com.baixingkuaizu.live.android.databinding.BaixingProfileFragmentBinding
import com.baixingkuaizu.live.android.busiess.profilefragment.Baixing_ProfileMenuAdapter

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 个人资料页面Fragment
 */
class Baixing_ProfileFragment : Baixing_BaseFragment() {
    
    private var mBaixing_binding: BaixingProfileFragmentBinding? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_binding = BaixingProfileFragmentBinding.inflate(inflater, container, false)
        return mBaixing_binding?.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baixing_initView()
    }
    
    private fun baixing_initView() {
        baixing_initUserInfo()
        baixing_initMemberInfo()
        baixing_initWalletInfo()
        baixing_initMenuGrid()
    }
    
    private fun baixing_initUserInfo() {
        mBaixing_binding?.apply {
            baixingProfileNickname.text = "超级颜十三"
            baixingProfileUserId.text = "ID:128297340"
            baixingProfileLevelTag.text = "一星"
            
            baixingProfileSwitchAccount.setOnClickListener {
                Toast.makeText(context, "点击了切换账号按钮", Toast.LENGTH_SHORT).show()
            }
            
            baixingProfileSettings.setOnClickListener {
                Toast.makeText(context, "点击了设置按钮", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun baixing_initMemberInfo() {
        mBaixing_binding?.apply {
            baixingProfileMemberLevel.text = "青铜"
            baixingProfileMemberDesc.text = "保级成功！距离白银还需充值80.0元"
            baixingProfileMemberExpire.text = "会员身份2025.03.23将重新结算"
        }
    }
    
    private fun baixing_initWalletInfo() {
        mBaixing_binding?.apply {
            baixingProfileWalletBalance.text = "¥0.00"
            baixingProfileRecharge.setOnClickListener {
                Toast.makeText(context, "点击了充值按钮", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun baixing_initMenuGrid() {
        val menuItems = Baixing_ProfileData.getDefaultMenuItems()
        val menuAdapter = Baixing_ProfileMenuAdapter(menuItems) { menuItem ->
            Toast.makeText(context, "点击了${menuItem.baixing_title}", Toast.LENGTH_SHORT).show()
            when (menuItem.baixing_action) {
                "follow" -> { /* 处理关注 */ }
                "history" -> { /* 处理历史记录 */ }
                "favorite" -> { /* 处理收藏 */ }
                "message" -> { /* 处理消息 */ }
                "settings" -> { /* 处理设置 */ }
                "help" -> { /* 处理帮助 */ }
                "about" -> { /* 处理关于 */ }
                "contact" -> { /* 处理联系客服 */ }
            }
        }
        
        mBaixing_binding?.baixingProfileFeatureMenu?.apply {
            layoutManager = GridLayoutManager(context, 4) // 4列网格
            adapter = menuAdapter
        }
        
        mBaixing_binding?.baixingProfileCommonMenu?.apply {
            layoutManager = GridLayoutManager(context, 4) // 4列网格
            adapter = menuAdapter
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        mBaixing_binding = null
    }
}