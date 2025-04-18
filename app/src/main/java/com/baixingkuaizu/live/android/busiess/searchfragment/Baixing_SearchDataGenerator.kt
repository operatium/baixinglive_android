package com.baixingkuaizu.live.android.busiess.searchfragment

import kotlin.random.Random

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 搜索数据生成器，用于生成模拟的主播数据
 */
class Baixing_SearchDataGenerator {
    private val mBaixing_categories = listOf("么么星球", "游戏", "音乐", "舞蹈", "美食", "旅游", "体育", "科技", "教育", "娱乐")
    private val mBaixing_anchorNames = listOf("艾薇儿123", "小甜甜", "舞动精灵", "游戏达人", "音乐小天后", "美食猎人", "旅行家", "体育健将", "科技达人", "教育先锋")
    
    /**
     * 根据关键词生成搜索结果列表
     * @param keyword 搜索关键词
     * @return 包含随机生成的主播数据的列表
     */
    fun baixing_generateSearchResults(keyword: String): List<Baixing_SearchAnchorEntity> {
        val resultCount = Random.nextInt(5, 15) // 随机生成5-15个结果
        val results = mutableListOf<Baixing_SearchAnchorEntity>()
        
        for (i in 0 until resultCount) {
            val anchorName = if (keyword.isNotEmpty() && Random.nextBoolean()) {
                "${keyword}${mBaixing_anchorNames.random()}"
            } else {
                mBaixing_anchorNames.random()
            }
            
            results.add(
                Baixing_SearchAnchorEntity(
                    id = "search_${i}_${System.currentTimeMillis()}",
                    anchorName = anchorName,
                    coverUrl = "https://picsum.photos/300/200?random=${i}_${System.currentTimeMillis()}",
                    category = mBaixing_categories.random(),
                    viewerCount = Random.nextInt(100, 50000),
                    isLive = Random.nextBoolean()
                )
            )
        }
        
        return results
    }
    
    /**
     * 生成单个随机主播数据
     * @return 随机生成的主播数据
     */
    fun baixing_generateRandomAnchor(): Baixing_SearchAnchorEntity {
        val index = Random.nextInt(0, 1000)
        return Baixing_SearchAnchorEntity(
            id = "anchor_$index",
            anchorName = mBaixing_anchorNames.random(),
            coverUrl = "https://picsum.photos/300/200?random=$index",
            category = mBaixing_categories.random(),
            viewerCount = Random.nextInt(100, 50000),
            isLive = Random.nextBoolean()
        )
    }
}