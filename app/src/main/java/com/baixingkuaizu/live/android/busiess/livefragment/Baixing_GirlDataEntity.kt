package com.baixingkuaizu.live.android.busiess.livefragment

import com.baixingkuaizu.live.android.base.Baixing_Entity

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播数据模型
 */
data class Baixing_GirlDataEntity(
    val id: String,
    val coverUrl: String,
    val anchorName: String,
    val viewerCount: Int
): Baixing_Entity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Baixing_GirlDataEntity

        if (viewerCount != other.viewerCount) return false
        if (id != other.id) return false
        if (coverUrl != other.coverUrl) return false
        if (anchorName != other.anchorName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = viewerCount
        result = 31 * result + id.hashCode()
        result = 31 * result + coverUrl.hashCode()
        result = 31 * result + anchorName.hashCode()
        return result
    }
}