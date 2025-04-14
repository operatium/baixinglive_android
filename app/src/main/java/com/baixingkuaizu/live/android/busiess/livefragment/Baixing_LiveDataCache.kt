package com.baixingkuaizu.live.android.busiess.livefragment

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播数据缓存类，提供模拟数据
 */
class Baixing_LiveDataCache private constructor() {
    private val mBaixing_liveDataList = mutableListOf<Baixing_LiveData>()

    init {
        // 初始化模拟数据
        for (i in 1..20) {
            mBaixing_liveDataList.add(
                Baixing_LiveData(
                    id = i.toString(),
                    anchorName = "主播 $i",
                    coverUrl = "https://picsum.photos/300/200?random=$i",
                    viewerCount = (1000..50000).random(),
                )
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: Baixing_LiveDataCache? = null

        fun getInstance(): Baixing_LiveDataCache {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Baixing_LiveDataCache().also { INSTANCE = it }
            }
        }
    }

    fun baixing_getLiveDataList(): List<Baixing_LiveData> {
        return mBaixing_liveDataList
    }

    fun baixing_refreshData() {
        mBaixing_liveDataList.clear()
        for (i in 1..20) {
            mBaixing_liveDataList.add(
                Baixing_LiveData(
                    id = i.toString(),
                    anchorName = "主播 $i",
                    coverUrl = "https://picsum.photos/300/200?random=${System.currentTimeMillis() + i}",
                    viewerCount = (1000..50000).random(),
                )
            )
        }
    }
}