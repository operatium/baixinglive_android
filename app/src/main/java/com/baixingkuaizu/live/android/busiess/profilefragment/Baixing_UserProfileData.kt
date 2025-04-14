package com.baixingkuaizu.live.android.busiess.profilefragment

import com.baixingkuaizu.live.android.base.Baixing_Entity

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 用户个人资料数据模型类
 */
data class Baixing_UserProfileData(
    val mBaixing_userId: String,           // 用户ID
    val mBaixing_nickname: String,         // 用户昵称
    val mBaixing_avatarUrl: String,        // 用户头像URL
    val mBaixing_memberLevel: String,      // 会员等级
    val mBaixing_memberExpireDate: String, // 会员到期日期
    val mBaixing_memberDescription: String,// 会员描述
    val mBaixing_walletBalance: Double     // 钱包余额
) : Baixing_Entity()