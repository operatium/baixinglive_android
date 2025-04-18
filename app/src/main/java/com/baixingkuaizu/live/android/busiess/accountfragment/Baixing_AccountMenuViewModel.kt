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

    private val _mBaixing_menuItems = MutableLiveData<List<Baixing_AccountMenuItem>>()
    val mBaixing_menuItems: LiveData<List<Baixing_AccountMenuItem>> = _mBaixing_menuItems

    init {
        baixing_initMenuItems()
    }

    private fun baixing_initMenuItems() {
    }

    fun baixing_onSettingClick() {
    }

    fun baixing_onWalletClick() {
    }

    fun baixing_onHistoryClick() {
    }

    fun baixing_onFavoriteClick() {
    }

    fun baixing_onHelpClick() {
    }

    fun baixing_onFeedbackClick() {
    }
}