package com.baixingkuaizu.live.android.busiess.task.login

import android.content.Context
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 登录任务管理类，负责管理登录任务和账号切换
 */
object Baixing_LoginTaskManager {
    private var mBaixing_currentTask: Baixing_LoginTask? = null

    private var mBaixing_ID = AtomicInteger(0)

    private var mBaixing_loginInfo: Baixing_LoginData? = null

    private val mBaixing_globalListener: Baixing_LoginTaskListener = object : Baixing_LoginTaskListener {
        override fun baixing_onStartTask(task: Baixing_LoginTask) {}

        override fun baixing_onEndTask(task: Baixing_LoginTask) {
            if (task == mBaixing_currentTask) {
                mBaixing_loginInfo = task.mBaixing_login
                mBaixing_currentTask = null
            }
        }

        override fun baixing_onLoginError(task: Baixing_LoginTask) {
            if (task == mBaixing_currentTask) {
                mBaixing_currentTask = null
            }
        }
        override fun baixing_onCancelTask(task: Baixing_LoginTask) {
            if (task == mBaixing_currentTask) {
                mBaixing_currentTask = null
            }
        }

    }

    fun baixing_obtainID(): Int {
        return mBaixing_ID.getAndIncrement()
    }

    /**
     * 创建新的登录任务
     */
    private fun baixing_createLoginTask(
        appContext: Context,
        phone: String,
        code: String,
        listener: Baixing_LoginTaskListener
    ): Baixing_LoginTask {
        return Baixing_LoginTask(
            appContext = appContext,
            taskName = "phone_login_${baixing_obtainID()}",
            mBaixing_login = Baixing_LoginData(mBaixing_phone = phone),
            mBaixing_code = code
        ).also {
            it.addListener("self", listener)
            it.addListener("global", mBaixing_globalListener)
            mBaixing_currentTask = it
        }
    }

    suspend fun baixing_loginAccount(
        appContext: Context,
        phone: String,
        code: String,
        listener: Baixing_LoginTaskListener
    ): String {
        return baixing_createLoginTask(appContext, phone, code, listener).baixing_login()
    }

    fun baixing_cancelLoginTask() {
        mBaixing_currentTask?.baixing_cancel()
        mBaixing_currentTask = null
    }
}