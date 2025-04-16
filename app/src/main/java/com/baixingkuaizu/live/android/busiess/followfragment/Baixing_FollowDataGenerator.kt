package com.baixingkuaizu.live.android.busiess.followfragment

class Baixing_FollowDataGenerator {
    /**
     * 随机生成一个Baixing_FollowGirlEntity对象
     * @param id 可选的ID，如果不提供则随机生成
     * @return 随机生成的Baixing_FollowGirlEntity对象
     */
    fun baixing_generateRandomFollowGirl(id: Int = (1..1000).random()): Baixing_FollowGirlEntity {
        val names = listOf(
            "莹莹不负遇见", "小鱼儿", "甜心宝贝", "星空漫步", "樱花雨",
            "微笑天使", "梦幻精灵", "清风徐来", "阳光女孩", "月光宝盒",
            "蓝色海洋", "紫色梦境", "红颜知己", "温柔如水", "冰雪公主"
        )

        return Baixing_FollowGirlEntity(
            id = id,
            name = names.random(),
            image = "https://picsum.photos/200/200?random=$id",
            isOnLine = (0..1).random() == 1, // 随机在线状态
            attention = (10..100).random() // 随机关注数
        )
    }
}