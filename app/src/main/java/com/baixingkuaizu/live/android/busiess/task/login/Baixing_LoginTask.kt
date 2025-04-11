package com.baixingkuaizu.live.android.busiess.task.login

import android.content.Context
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.busiess.task.Baixing_CoreWork

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 登录任务类，负责处理登录、登出功能和任务生命周期
 */
open class Baixing_LoginTask(
    appContext: Context,
    private var mBaixing_login: Baixing_LoginData? = null,
    private val mBaixing_code: String,
) {
    var mBaixing_listener: Baixing_LoginTaskListener? = null
    private val mBaixing_localDataManager: Baixing_LocalDataManager = Baixing_LocalDataManager.baixing_getInstance(appContext)

    init {
        mBaixing_listener?.baixing_onCreateTask(this)
    }

    fun baixing_login(): Boolean {
        mBaixing_login?.let {
            mBaixing_listener?.baixing_onStartTask(this)
            it.mBaixing_token = Baixing_CoreWork.baixing_login(it.mBaixing_phone, mBaixing_code)
            mBaixing_localDataManager.baixing_setLoginToken(it.mBaixing_token)
            mBaixing_listener?.baixing_onEndTask(this)
            return true
        }
        return false
    }

    fun baixing_logout() {
        mBaixing_listener?.baixing_onStopTask(this)
        if (Baixing_CoreWork.baixing_logout()) {
            mBaixing_login = null
            mBaixing_localDataManager.baixing_clearLoginToken()
            mBaixing_listener?.baixing_onDestroyTask(this)
        }
    }

    /**
     * 获取当前登录token
     */
    fun baixing_getToken(): String = mBaixing_login?.mBaixing_token?:""
}