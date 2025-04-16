package com.baixingkuaizu.live.android.busiess.messagefragment

import java.util.UUID

/**
 * @author yuyuexing
 * @since 2025/4/18
 * @description: 聊天数据生成器，用于生成模拟聊天数据
 */
class Baixing_MessageDataGenerator {
    companion object {
        private const val SYSTEM_AVATAR = "https://img.baixingkuaizu.com/avatar/system_avatar.png"
        private const val SERVICE_AVATAR = "https://img.baixingkuaizu.com/avatar/service_avatar.png"
        private const val DEFAULT_AVATAR = "https://img.baixingkuaizu.com/avatar/default_avatar.png"
    }

    fun baixing_generateSystemMessages(count: Int = 3): List<Baixing_MessageItemEntity> {
        val messages = mutableListOf<Baixing_MessageItemEntity>()
        val titles = listOf("系统通知", "活动通知", "更新提醒")
        val contents = listOf(
            "欢迎使用百姓快租直播，您的账号已激活", 
            "五一特惠活动开始啦，点击查看详情", 
            "新版本已发布，请及时更新体验新功能"
        )
        
        for (i in 0 until count) {
            val timestamp = System.currentTimeMillis() - (i * 3600000)
            messages.add(
                Baixing_MessageItemEntity(
                    baixing_id = "system_${UUID.randomUUID()}",
                    baixing_title = titles[i % titles.size],
                    baixing_content = contents[i % contents.size],
                    baixing_timestamp = timestamp,
                    baixing_avatarUrl = SYSTEM_AVATAR,
                    baixing_unreadCount = if (i == 0) 1 else 0,
                    baixing_isOfficial = true,
                    baixing_isMemberService = false
                )
            )
        }
        
        return messages
    }

    fun baixing_generateServiceMessages(count: Int = 2): List<Baixing_MessageItemEntity> {
        val messages = mutableListOf<Baixing_MessageItemEntity>()
        val titles = listOf("在线客服", "会员服务")
        val contents = listOf(
            "您好，有什么可以帮助您的吗？", 
            "尊敬的用户，您的会员即将到期，请及时续费"
        )
        
        for (i in 0 until count) {
            val timestamp = System.currentTimeMillis() - (i * 7200000)
            messages.add(
                Baixing_MessageItemEntity(
                    baixing_id = "service_${UUID.randomUUID()}",
                    baixing_title = titles[i % titles.size],
                    baixing_content = contents[i % contents.size],
                    baixing_timestamp = timestamp,
                    baixing_avatarUrl = SERVICE_AVATAR,
                    baixing_unreadCount = if (i == 0) 2 else 0,
                    baixing_isOfficial = false,
                    baixing_isMemberService = true
                )
            )
        }
        
        return messages
    }

    fun baixing_generateUserMessages(count: Int = 5): List<Baixing_MessageItemEntity> {
        val messages = mutableListOf<Baixing_MessageItemEntity>()
        val names = listOf("张先生", "李女士", "王先生", "赵女士", "刘先生")
        val contents = listOf(
            "您好，我想咨询一下这个房源还在吗？", 
            "请问什么时候可以看房？", 
            "这个价格能再便宜一点吗？", 
            "房子的位置在哪里？离地铁站远吗？",
            "我已经看过房了，很满意，什么时候可以签约？"
        )
        val avatars = listOf(
            "https://i.pravatar.cc/150?img=1",
            "https://i.pravatar.cc/150?img=2",
            "https://i.pravatar.cc/150?img=3",
            "https://i.pravatar.cc/150?img=4",
            "https://i.pravatar.cc/150?img=5"
        )
        
        for (i in 0 until count) {
            val timestamp = System.currentTimeMillis() - (i * 1800000)
            messages.add(
                Baixing_MessageItemEntity(
                    baixing_id = "user_${UUID.randomUUID()}",
                    baixing_title = names[i % names.size],
                    baixing_content = contents[i % contents.size],
                    baixing_timestamp = timestamp,
                    baixing_avatarUrl = avatars.getOrElse(i) { DEFAULT_AVATAR },
                    baixing_unreadCount = if (i < 2) i + 1 else 0,
                    baixing_isOfficial = false,
                    baixing_isMemberService = false
                )
            )
        }
        
        return messages
    }

    fun baixing_generateAllMessages(): List<Baixing_MessageItemEntity> {
        val allMessages = mutableListOf<Baixing_MessageItemEntity>().apply {
            addAll(baixing_generateSystemMessages())
            addAll(baixing_generateServiceMessages())
            addAll(baixing_generateUserMessages())
        }
        
        return allMessages.sortedByDescending { it.baixing_timestamp }
    }
}