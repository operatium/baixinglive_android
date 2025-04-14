package com.baixingkuaizu.live.android.busiess.livefragment

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播栏目数据缓存类，提供10个栏目及其下的直播数据
 */
class Baixing_LiveListCache private constructor() {
    // 栏目列表
    private val mBaixing_categoryList = mutableListOf<Baixing_CategoryData>()
    // 每个栏目下的直播数据
    private val mBaixing_liveDataMap = mutableMapOf<String, MutableList<Baixing_LiveData>>()
    // 是否有更多数据的标记
    private val mBaixing_hasMoreMap = mutableMapOf<String, Boolean>()
    // 当前页码
    private val mBaixing_currentPageMap = mutableMapOf<String, Int>()
    // 每页数据量
    private val mBaixing_pageSize = 10
    
    init {
        // 初始化10个栏目
        val categories = listOf(
            "推荐", "游戏", "音乐", "舞蹈", "美食", 
            "旅游", "体育", "科技", "教育", "娱乐"
        )
        
        // 创建栏目数据
        categories.forEachIndexed { index, name ->
            val categoryId = "category_$index"
            mBaixing_categoryList.add(Baixing_CategoryData(categoryId, name))
            mBaixing_liveDataMap[categoryId] = mutableListOf()
            mBaixing_hasMoreMap[categoryId] = true
            mBaixing_currentPageMap[categoryId] = 1
            
            // 初始化第一页数据
            baixing_generateLiveData(categoryId, 1)
        }
    }
    
    companion object {
        @Volatile
        private var INSTANCE: Baixing_LiveListCache? = null
        
        fun getInstance(): Baixing_LiveListCache {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Baixing_LiveListCache().also { INSTANCE = it }
            }
        }
    }
    
    /**
     * 获取所有栏目数据
     */
    fun baixing_getCategoryList(): List<Baixing_CategoryData> {
        return mBaixing_categoryList
    }
    
    /**
     * 获取指定栏目下的直播数据
     */
    fun baixing_getLiveDataList(categoryId: String): List<Baixing_LiveData> {
        return mBaixing_liveDataMap[categoryId] ?: emptyList()
    }
    
    /**
     * 刷新指定栏目的数据
     */
    fun baixing_refreshData(categoryId: String) {
        mBaixing_liveDataMap[categoryId]?.clear()
        mBaixing_currentPageMap[categoryId] = 1
        mBaixing_hasMoreMap[categoryId] = true
        baixing_generateLiveData(categoryId, 1)
    }
    
    /**
     * 加载更多数据
     * @return 是否成功加载了更多数据
     */
    fun baixing_loadMoreData(categoryId: String): Boolean {
        if (mBaixing_hasMoreMap[categoryId] != true) {
            return false
        }
        
        val currentPage = mBaixing_currentPageMap[categoryId] ?: 1
        val nextPage = currentPage + 1
        
        // 模拟数据加载，最多加载5页
        if (nextPage > 5) {
            mBaixing_hasMoreMap[categoryId] = false
            return false
        }
        
        baixing_generateLiveData(categoryId, nextPage)
        mBaixing_currentPageMap[categoryId] = nextPage
        return true
    }
    
    /**
     * 检查是否还有更多数据
     */
    fun baixing_hasMoreData(categoryId: String): Boolean {
        return mBaixing_hasMoreMap[categoryId] ?: false
    }
    
    /**
     * 生成模拟直播数据
     */
    private fun baixing_generateLiveData(categoryId: String, page: Int) {
        val categoryIndex = mBaixing_categoryList.indexOfFirst { it.id == categoryId }
        val categoryName = mBaixing_categoryList[categoryIndex].name
        val startIndex = (page - 1) * mBaixing_pageSize
        
        val liveDataList = mBaixing_liveDataMap[categoryId] ?: return
        
        for (i in 1..mBaixing_pageSize) {
            val index = startIndex + i
            liveDataList.add(
                Baixing_LiveData(
                    id = "${categoryId}_$index",
                    anchorName = "${categoryName}主播$index",
                    coverUrl = "https://picsum.photos/300/200?random=${System.currentTimeMillis() + index}",
                    viewerCount = (1000..50000).random()
                )
            )
        }
    }
}