package com.baixingkuaizu.live.android.busiess.profilefragment

import com.baixingkuaizu.live.android.base.Baixing_Entity

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 个人资料页面功能菜单项数据模型类
 */
data class Baixing_FunctionMenuItem(
    val mBaixing_id: Int,             // 菜单项ID
    val mBaixing_title: String,       // 菜单项标题
    val mBaixing_iconResId: Int,      // 菜单项图标资源ID
    val mBaixing_type: MenuType       // 菜单项类型
) : Baixing_Entity() {
    
    /**
     * 菜单类型枚举
     */
    enum class MenuType {
        FEATURE,    // 特色功能（装扮、神行百变等）
        COMMON      // 常用功能（我看过的、99家族等）
    }
}