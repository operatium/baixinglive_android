package com.baixingkuaizu.live.android.http

import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_CategoryDataEntity
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveDataEntity
import com.baixingkuaizu.live.android.busiess.messagefragment.Baixing_MessageDataGenerator
import com.baixingkuaizu.live.android.busiess.messagefragment.Baixing_MessageItemEntity
import com.baixingkuaizu.live.android.busiess.followfragment.Baixing_FollowDataGenerator
import com.baixingkuaizu.live.android.busiess.followfragment.Baixing_FollowGirlEntity
import com.baixingkuaizu.live.android.busiess.searchfragment.Baixing_SearchAnchorEntity
import com.baixingkuaizu.live.android.busiess.searchfragment.Baixing_SearchDataGenerator
import kotlinx.coroutines.delay

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 核心任务类，负责处理核心业务逻辑
 */
object Baixing_CoreWork {
    var mBaixing_HttpTimeout = 5000L

    //发送验证码
    suspend fun baixing_sendVerificationCode():String {
        delayNet()
        return "000000"
    }

    //登陆
    suspend fun baixing_login(phone:String, code:String):String {
        delayNet()
        return "userToken"
    }

    //登出
    suspend fun baixing_logout():Boolean {
        delayNet()
        return true
    }

    suspend fun baixing_liveTabs(): ArrayList<Baixing_CategoryDataEntity> {
        delayNet()
        return listOf(
            "推荐", "游戏", "音乐", "舞蹈", "美食",
            "旅游", "体育", "科技", "教育", "娱乐"
        ).map {  Baixing_CategoryDataEntity(it, it) }.toList().let {
            ArrayList(it)
        }
    }

    //请求列表
    suspend fun baixing_liveList(id:String, page:Int): ArrayList<Baixing_LiveDataEntity> {
        delayNet()
        val list = ArrayList<Baixing_LiveDataEntity>()
        if (page > 3) {
            return list
        }
        for (index in 0 .. 9) {
            val i = index + page * 10
            list.add(Baixing_LiveDataEntity(
                id = i.toString(),
                anchorName = "主播 $i",
                coverUrl = "https://picsum.photos/300/200?random=$i",
                viewerCount = (1000..50000).random(),
            ))
        }
        return list
    }

    suspend fun baixing_messageUserList(): ArrayList<Baixing_MessageItemEntity> {
        delayNet()
        val generator = Baixing_MessageDataGenerator()
        return ArrayList(generator.baixing_generateAllMessages())
    }
    
    /**
     * 获取关注列表
     * @return 包含10个随机生成的Baixing_FollowGirlEntity对象的列表
     */
    suspend fun baixing_followList(): ArrayList<Baixing_FollowGirlEntity> {
        delayNet()
        val generator = Baixing_FollowDataGenerator()
        val list = ArrayList<Baixing_FollowGirlEntity>()
        
        // 生成10个随机的关注对象
        repeat(10) {
            list.add(generator.baixing_generateRandomFollowGirl())
        }
        
        return list
    }
    
    /**
     * 搜索主播
     * @param keyword 搜索关键词
     * @return 包含搜索结果的主播列表
     */
    suspend fun baixing_searchAnchor(keyword: String): ArrayList<Baixing_SearchAnchorEntity> {
        delayNet()
        val generator = Baixing_SearchDataGenerator()
        return ArrayList(generator.baixing_generateSearchResults(keyword))
    }

    private suspend fun delayNet(time:Long = 5000) {
        delay((0..1000).random().toLong())
    }
}