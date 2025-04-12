package com.baixingkuaizu.live.android.busiess.teenmode

import com.baixingkuaizu.live.android.base.Baixing_Entity

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
    val mBaixing_tag: String,
    val mBaixing_videoUrl: String = "https://media.w3.org/2010/05/sintel/trailer.mp4" // 默认提供一个示例视频URL
):Baixing_Entity()