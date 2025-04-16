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
): Baixing_Entity()