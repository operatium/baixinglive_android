package com.baixingkuaizu.live.android.busiess.messagefragment

import com.baixingkuaizu.live.android.base.Baixing_Entity

data class Baixing_MessageItemEntity(
    val baixing_id: String,
    val baixing_title: String,
    val baixing_content: String,
    val baixing_timestamp: Long,
    val baixing_avatarUrl: String,
    val baixing_unreadCount: Int = 0,
    val baixing_isOfficial: Boolean = false,
    val baixing_isMemberService: Boolean = false
): Baixing_Entity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Baixing_MessageItemEntity

        if (baixing_timestamp != other.baixing_timestamp) return false
        if (baixing_unreadCount != other.baixing_unreadCount) return false
        if (baixing_isOfficial != other.baixing_isOfficial) return false
        if (baixing_isMemberService != other.baixing_isMemberService) return false
        if (baixing_id != other.baixing_id) return false
        if (baixing_title != other.baixing_title) return false
        if (baixing_content != other.baixing_content) return false
        if (baixing_avatarUrl != other.baixing_avatarUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = baixing_timestamp.hashCode()
        result = 31 * result + baixing_unreadCount
        result = 31 * result + baixing_isOfficial.hashCode()
        result = 31 * result + baixing_isMemberService.hashCode()
        result = 31 * result + baixing_id.hashCode()
        result = 31 * result + baixing_title.hashCode()
        result = 31 * result + baixing_content.hashCode()
        result = 31 * result + baixing_avatarUrl.hashCode()
        return result
    }
}