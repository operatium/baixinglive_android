package com.baixingkuaizu.live.android.busiess.teenmode

class Baixing_VideoDataCache {
    
    private val mBaixing_allVideoList = listOf(
        Baixing_VideoData(
            mBaixing_id = "1",
            mBaixing_title = "探索自然奥秘：动物世界大揭秘",
            mBaixing_author = "自然探索频道",
            mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
            mBaixing_duration = "05:30",
            mBaixing_tag = "自然"
        ),
        Baixing_VideoData(
            mBaixing_id = "2",
            mBaixing_title = "科学启蒙：奇妙化学实验",
            mBaixing_author = "科学课堂",
            mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
            mBaixing_duration = "08:15",
            mBaixing_tag = "科普"
        ),
        Baixing_VideoData(
            mBaixing_id = "3",
            mBaixing_title = "数学思维训练：几何图形的奥秘",
            mBaixing_author = "数学教育",
            mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
            mBaixing_duration = "10:22",
            mBaixing_tag = "教育"
        ),
        Baixing_VideoData(
            mBaixing_id = "4",
            mBaixing_title = "古代文明探索：埃及金字塔之谜",
            mBaixing_author = "历史探秘",
            mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
            mBaixing_duration = "12:45",
            mBaixing_tag = "历史"
        ),
        Baixing_VideoData(
            mBaixing_id = "5",
            mBaixing_title = "动画片：小熊历险记 第一集",
            mBaixing_author = "儿童动画工作室",
            mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
            mBaixing_duration = "15:00",
            mBaixing_tag = "动画"
        ),
        Baixing_VideoData(
            mBaixing_id = "6",
            mBaixing_title = "自然纪录片：海洋生物的奇妙世界",
            mBaixing_author = "自然探索频道",
            mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
            mBaixing_duration = "18:30",
            mBaixing_tag = "自然"
        ),
        Baixing_VideoData(
            mBaixing_id = "7",
            mBaixing_title = "物理知识：简单机械原理",
            mBaixing_author = "科学课堂",
            mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
            mBaixing_duration = "07:45",
            mBaixing_tag = "科普"
        ),
        Baixing_VideoData(
            mBaixing_id = "8",
            mBaixing_title = "语文名师讲解：古诗词鉴赏",
            mBaixing_author = "语文教育",
            mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
            mBaixing_duration = "09:50",
            mBaixing_tag = "教育"
        )
    )
    
    private var mBaixing_currentVideoList: List<Baixing_VideoData> = mBaixing_allVideoList
    
    fun baixing_getAllVideos(): List<Baixing_VideoData> {
        return mBaixing_allVideoList
    }
    
    fun baixing_getCurrentVideos(): List<Baixing_VideoData> {
        return mBaixing_currentVideoList
    }
    
    fun baixing_filterByTag(tag: String): List<Baixing_VideoData> {
        mBaixing_currentVideoList = if (tag.isEmpty() || tag == "全部") {
            mBaixing_allVideoList
        } else {
            mBaixing_allVideoList.filter { it.mBaixing_tag == tag }
        }
        return mBaixing_currentVideoList
    }
    
    fun baixing_getAllTags(): List<String> {
        return mBaixing_allVideoList.map { it.mBaixing_tag }.distinct()
    }
    
    fun baixing_findVideoById(id: String): Baixing_VideoData? {
        return mBaixing_allVideoList.find { it.mBaixing_id == id }
    }
    
    companion object {
        private var INSTANCE: Baixing_VideoDataCache? = null
        
        fun getInstance(): Baixing_VideoDataCache {
            if (INSTANCE == null) {
                INSTANCE = Baixing_VideoDataCache()
            }
            return INSTANCE!!
        }
    }
}