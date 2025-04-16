package com.baixingkuaizu.live.android.busiess.accountfragment

import com.baixingkuaizu.live.android.base.Baixing_Entity

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 账户菜单项数据模型类
 */
data class Baixing_AccountMenuItem(
    val mBaixing_id: Int,             // 菜单项ID
    val mBaixing_title: String,       // 菜单项标题
    val mBaixing_iconResId: Int,      // 菜单项图标资源ID
    val mBaixing_type: MenuType       // 菜单项类型
) : Baixing_Entity() {
    
    /**
     * 菜单类型枚举
     */
    enum class MenuType {
        SETTING,    // 设置类
        FEATURE,    // 功能类
        SERVICE     // 服务类
    }
}