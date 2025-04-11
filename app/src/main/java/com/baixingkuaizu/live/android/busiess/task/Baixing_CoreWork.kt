package com.baixingkuaizu.live.android.busiess.task

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 核心任务类，负责处理核心业务逻辑
 */
object Baixing_CoreWork {

    //发送验证码
    fun baixing_sendVerificationCode():String {
        Thread.sleep(5000)
        return "000000"
    }

    //登陆
    fun baixing_login():String {
        Thread.sleep(5000)
        return "userToken"
    }
}