package com.baixingkuaizu.live.android.http

import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_CategoryDataEntity
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveDataEntity
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

    private suspend fun delayNet(time:Long = 5000) {
        delay((0..6000).random().toLong())
    }
}