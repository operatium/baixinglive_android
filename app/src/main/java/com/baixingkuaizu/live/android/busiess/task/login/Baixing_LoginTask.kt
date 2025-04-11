package com.baixingkuaizu.live.android.busiess.task.login

import android.content.Context
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.busiess.task.Baixing_BaseTask
import com.baixingkuaizu.live.android.busiess.task.Baixing_CoreWork
import java.util.Collections

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 登录任务类，负责处理登录、登出功能和任务生命周期
 */
open class Baixing_LoginTask(
    appContext: Context,
    taskName: String,
    var mBaixing_login: Baixing_LoginData,
    private val mBaixing_code: String,
) : Baixing_BaseTask(taskName) {
    private val mBaixing_listeners: MutableMap<String, Baixing_LoginTaskListener> =
        Collections.synchronizedMap(HashMap())
    private val mBaixing_localDataManager: Baixing_LocalDataManager =
        Baixing_LocalDataManager.baixing_getInstance(appContext)

    suspend fun baixing_login(): String {
        mBaixing_login.let {
            baixing_onStartTask()
            it.mBaixing_token = Baixing_CoreWork.baixing_login(it.mBaixing_phone, mBaixing_code)
            mBaixing_localDataManager.baixing_setLoginToken(it.mBaixing_token)
            baixing_onEndTask()
            return it.mBaixing_token
        }
    }

    override fun baixing_cancel() {
        super.baixing_cancel()
        baixing_onCancelTask()
        mBaixing_login.mBaixing_token = ""
    }

    /**
     * 获取当前登录token
     */
    fun baixing_getToken(): String = mBaixing_login.mBaixing_token

    override fun baixing_onStartTask() {
        if (mBaixing_cancel) return
        mBaixing_listeners.values.forEach { it.baixing_onStartTask(this) }
    }

    override fun baixing_onEndTask() {
        if (mBaixing_cancel) return
        mBaixing_listeners.values.forEach { it.baixing_onEndTask(this) }
    }

    fun baixing_onLoginError() {
        if (mBaixing_cancel) return
        mBaixing_listeners.values.forEach { it.baixing_onLoginError(this) }
    }

    fun baixing_onCancelTask() {
        mBaixing_listeners.values.forEach { it.baixing_onCancelTask(this) }
        mBaixing_listeners.clear()
    }

    fun addListener(key: String, listener: Baixing_LoginTaskListener) {
        mBaixing_listeners[key] = listener
    }

    fun removeListener(key: String) {
        mBaixing_listeners.remove(key)
    }
}