package com.baixingkuaizu.live.android.busiess.task.login

import android.content.Context
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.busiess.task.Baixing_BaseTask
import com.baixingkuaizu.live.android.busiess.task.Baixing_CoreWork

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 登录任务类，负责处理登录、登出功能和任务生命周期
 */
open class Baixing_LoginTask(
    appContext: Context,
    taskName: String,
    private var mBaixing_login: Baixing_LoginData? = null,
    private val mBaixing_code: String,
):Baixing_BaseTask(taskName) {
    var mBaixing_listener: Baixing_LoginTaskListener? = null
    private val mBaixing_localDataManager: Baixing_LocalDataManager =
        Baixing_LocalDataManager.baixing_getInstance(appContext)

    fun baixing_login(): Boolean {
        mBaixing_login?.let {
            baixing_onStartTask()
            it.mBaixing_token = Baixing_CoreWork.baixing_login(it.mBaixing_phone, mBaixing_code)
            mBaixing_localDataManager.baixing_setLoginToken(it.mBaixing_token)
            baixing_onEndTask()
            return true
        }
        return false
    }

    fun baixing_logout() {
        baixing_onStopTask()
        if (Baixing_CoreWork.baixing_logout()) {
            mBaixing_login = null
            mBaixing_localDataManager.baixing_clearLoginToken()
            baixing_onDestroyTask()
        }
    }

    /**
     * 获取当前登录token
     */
    fun baixing_getToken(): String = mBaixing_login?.mBaixing_token ?: ""

    override fun baixing_onStartTask() {
        mBaixing_listener?.baixing_onStartTask(this)
    }

    override fun baixing_onEndTask() {
        mBaixing_listener?.baixing_onEndTask(this)
    }

    fun baixing_onLoginTimeOut() {
        mBaixing_listener?.baixing_onLoginTimeOut(this)
    }

    fun baixing_onLoginError() {
        mBaixing_listener?.baixing_onLoginError(this)
    }
}