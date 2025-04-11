package com.baixingkuaizu.live.android.busiess.task.login

import com.baixingkuaizu.live.android.busiess.task.Baixing_BaseTask
import com.baixingkuaizu.live.android.busiess.task.Baixing_CoreWork
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.ConcurrentHashMap

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 发送验证码任务类，负责处理验证码发送逻辑和验证手机号格式
 */
class Baixing_SendVerficationCodeTask(taskName: String, val phoneNumber: String) : Baixing_BaseTask(taskName) {
    private val mBaixing_TIME = 60000
    private val mBaixing_startTime: Long = System.currentTimeMillis()
    private var mBaixing_Timer: Timer? = null
    private val mBaixing_Listeners: ConcurrentHashMap<String, Baixing_SendVerficationCodeTaskListener> = ConcurrentHashMap()
    var mbaixing_code: String? = null

    suspend fun baixing_sendVerificationCode(): String {
        baixing_onStartTask()
        baixing_startCountdown()
        mbaixing_code = Baixing_CoreWork.baixing_sendVerificationCode()
        baixing_onEndTask()
        return mbaixing_code ?: ""
    }

    override fun baixing_cancel() {
        super.baixing_cancel()
        baixing_onCancelTask()
    }

    fun baixing_isTimeOver(): Boolean {
        val t = System.currentTimeMillis() - mBaixing_startTime
        return t > mBaixing_TIME
    }

    private fun baixing_getSecond4Countdown(): Int {
        return ((mBaixing_TIME - System.currentTimeMillis() + mBaixing_startTime) / 1000).toInt()
    }

    private fun baixing_startCountdown() {
        if (baixing_isTimeOver()) {
            return
        }
        if (mBaixing_Timer == null) {
            mBaixing_Timer = Timer()
            mBaixing_Timer?.schedule(object : TimerTask() {
                override fun run() {
                    if (mBaixing_cancel) {
                        mBaixing_Timer?.cancel()
                        mBaixing_Timer?.purge()
                        mBaixing_Timer = null
                        return
                    }
                    if (baixing_isTimeOver()) {
                        mBaixing_Timer?.cancel()
                        mBaixing_Timer?.purge()
                        mBaixing_Timer = null
                        baixing_onDestroyTask()
                        return
                    }
                    baixing_onTime(baixing_getSecond4Countdown())
                }
            }, 0, 1000)
        }
    }

    override fun baixing_onStartTask() {
        if (mBaixing_cancel) return
        mBaixing_Listeners.values.forEach { it.baixing_onStartTask(this) }
    }

    fun baixing_onTime(second: Int) {
        if (mBaixing_cancel) return
        mBaixing_Listeners.values.forEach { it.baixing_onTime(this, second) }
    }

    override fun baixing_onEndTask() {
        if (mBaixing_cancel) return
        mBaixing_Listeners.values.forEach { it.baixing_onEndTask(this) }
    }

    override fun baixing_onDestroyTask() {
        if (mBaixing_cancel) return
        mBaixing_Listeners.values.forEach { it.baixing_onDestroyTask(this) }
    }

    fun baixing_onCancelTask() {
        mBaixing_Listeners.values.forEach { it.baixing_onCancelTask(this) }
        mBaixing_Listeners.clear()
    }

    fun addListener(key: String, listener: Baixing_SendVerficationCodeTaskListener) {
        mBaixing_Listeners[key] = listener
    }

    fun removeListener(key: String) {
        mBaixing_Listeners.remove(key)
    }
}
