package com.baixingkuaizu.live.android.busiess.searchfragment

import com.baixingkuaizu.live.android.base.Baixing_Entity

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 搜索主播数据实体类，包含主播的基本信息
 */
data class Baixing_SearchAnchorEntity(
    val id: String,                // 主播ID
    val anchorName: String,        // 主播名称
    val coverUrl: String,          // 封面图片URL
    val category: String,          // 分类（如：么么星球）
    val viewerCount: Int,          // 观看人数
    val isLive: Boolean = true     // 是否正在直播
) : Baixing_Entity() {
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Baixing_SearchAnchorEntity

        if (id != other.id) return false
        if (anchorName != other.anchorName) return false
        if (coverUrl != other.coverUrl) return false
        if (category != other.category) return false
        if (viewerCount != other.viewerCount) return false
        if (isLive != other.isLive) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + anchorName.hashCode()
        result = 31 * result + coverUrl.hashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + viewerCount
        result = 31 * result + isLive.hashCode()
        return result
    }
}