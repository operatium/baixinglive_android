package com.baixingkuaizu.live.android.busiess.profilefragment

import android.R
import com.baixingkuaizu.live.android.base.Baixing_Entity

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 个人资料页面数据模型
 */
class Baixing_ProfileData : Baixing_Entity() {
    var baixing_userId: String = ""
    var baixing_nickname: String = ""
    var baixing_avatar: String = ""
    var baixing_level: String = ""
    
    var baixing_memberLevel: String = ""
    var baixing_memberDesc: String = ""
    var baixing_memberExpire: String = ""
    
    var baixing_balance: String = ""
    var baixing_diamonds: String = ""
    var baixing_coins: String = ""
    

    
    companion object {
        fun getDefaultMenuItems(): List<Baixing_MenuItem> {
            return listOf(
                Baixing_MenuItem(R.drawable.ic_menu_camera, "我的关注", "follow"),
                Baixing_MenuItem(R.drawable.ic_menu_gallery, "历史记录", "history"),
                Baixing_MenuItem(R.drawable.ic_menu_share, "我的收藏", "favorite"),
                Baixing_MenuItem(R.drawable.ic_menu_send, "我的消息", "message"),
                Baixing_MenuItem(R.drawable.ic_menu_manage, "设置", "settings"),
                Baixing_MenuItem(R.drawable.ic_menu_help, "帮助中心", "help"),
                Baixing_MenuItem(R.drawable.ic_menu_info_details, "关于我们", "about"),
                Baixing_MenuItem(R.drawable.ic_menu_call, "联系客服", "contact")
            )
        }
    }
}