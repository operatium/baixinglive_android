package com.baixingkuaizu.live.android.fragment

/**
 * @author yuyuexing
 * @date: 2025/4/14
 * @description: 视频数据模型类
 */
data class Baixing_VideoData(
    val mBaixing_id: String,
    val mBaixing_title: String,
    val mBaixing_author: String,
    val mBaixing_coverUrl: String = "",
    val mBaixing_duration: String,
    val mBaixing_tag: String
) 