package com.baixingkuaizu.live.android.busiess.task

import kotlinx.coroutines.delay

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 核心任务类，负责处理核心业务逻辑
 */
object Baixing_CoreWork {

    //发送验证码
    suspend fun baixing_sendVerificationCode():String {
        delay(5000)
        return "000000"
    }

    //登陆
    suspend fun baixing_login(phone:String, code:String):String {
        delay(5000)
        return "userToken"
    }

    //登出
    suspend fun baixing_logout():Boolean {
        delay(5000)
        return true
    }
}