package com.baixingkuaizu.live.android.busiess.livefragment

import com.baixingkuaizu.live.android.base.Baixing_Entity

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播数据模型
 */
data class Baixing_LiveDataEntity(
    val id: String,
    val coverUrl: String,
    val anchorName: String,
    val viewerCount: Int
): Baixing_Entity()