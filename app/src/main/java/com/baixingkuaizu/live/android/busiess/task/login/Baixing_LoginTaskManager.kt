package com.baixingkuaizu.live.android.busiess.task.login

import android.content.Context

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 登录任务管理类，负责管理登录任务和账号切换
 */
object Baixing_LoginTaskManager {
    private var mBaixing_currentTask: Baixing_LoginTask? = null

    /**
     * 创建新的登录任务
     */
    fun baixing_createLoginTask(
        appContext: Context,
        phone: String,
        code: String,
        listener: Baixing_LoginTaskListener
    ): Baixing_LoginTask {
        return Baixing_LoginTask(appContext, Baixing_LoginData(mBaixing_phone = phone), code).also {
            it.mBaixing_listener = listener
            mBaixing_currentTask = it
        }
    }

    fun baixing_getCurrentTask(): Baixing_LoginTask? = mBaixing_currentTask

    fun baixing_switchAccount(appContext: Context, phone: String, code: String,listener: Baixing_LoginTaskListener): Boolean {
        mBaixing_currentTask?.baixing_logout()
        return baixing_createLoginTask(appContext, phone, code, listener).baixing_login()
    }

    fun baixing_loginAccount(appContext: Context, phone: String, code: String,listener: Baixing_LoginTaskListener): Boolean {
        return baixing_createLoginTask(appContext, phone, code, listener).baixing_login()
    }
}