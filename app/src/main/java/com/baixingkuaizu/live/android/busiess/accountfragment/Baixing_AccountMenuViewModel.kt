package com.baixingkuaizu.live.android.busiess.accountfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 账户菜单页面的ViewModel，管理菜单数据和处理菜单点击事件
 */
class Baixing_AccountMenuViewModel : ViewModel() {

    // 菜单项数据
    private val _mBaixing_menuItems = MutableLiveData<List<Baixing_AccountMenuItem>>()
    val mBaixing_menuItems: LiveData<List<Baixing_AccountMenuItem>> = _mBaixing_menuItems

    init {
        // 初始化菜单数据
        baixing_initMenuItems()
    }

    private fun baixing_initMenuItems() {
        // 在实际应用中，可能会从数据库或网络加载菜单项
        // 这里为了简单，直接创建固定的菜单项列表
    }

    // 菜单项点击事件处理方法
    fun baixing_onSettingClick() {
        // 处理设置菜单点击
        // 实际应用中可能会跳转到设置页面
    }

    fun baixing_onWalletClick() {
        // 处理钱包菜单点击
        // 实际应用中可能会跳转到钱包页面
    }

    fun baixing_onHistoryClick() {
        // 处理观看历史菜单点击
        // 实际应用中可能会跳转到历史记录页面
    }

    fun baixing_onFavoriteClick() {
        // 处理我的收藏菜单点击
        // 实际应用中可能会跳转到收藏页面
    }

    fun baixing_onHelpClick() {
        // 处理帮助中心菜单点击
        // 实际应用中可能会跳转到帮助页面
    }

    fun baixing_onFeedbackClick() {
        // 处理意见反馈菜单点击
        // 实际应用中可能会跳转到反馈页面
    }
}