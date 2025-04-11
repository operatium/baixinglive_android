package com.baixingkuaizu.live.android.busiess.task.login

import com.baixingkuaizu.live.android.busiess.task.Baixing_TaskManager
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 验证码发送任务管理器，负责管理验证码发送任务的创建和执行
 */
object Baixing_SendVerficationCodeTaskManager: Baixing_TaskManager<Baixing_SendVerficationCodeTask>() {
    private const val TAG = "Baixing_SendVerficationCodeTaskManager"

    private var mBaixing_currentTask: Baixing_SendVerficationCodeTask? = null

    private var mBaixing_ID = AtomicInteger(0)

    fun baixing_obtainID(): Int {
        return mBaixing_ID.getAndIncrement()
    }

    fun sendVerificationCode(taskName:String, phone:String, listener: Baixing_SendVerficationCodeTaskListener) {
        mBaixing_currentTask = Baixing_SendVerficationCodeTask(taskName, phone).apply {
            mBaixing_Listener = listener
            baixing_sendVerificationCode()
        }
    }

    fun baixing_isValidPhoneNumber(phoneNumber:String): Boolean {
        val regex = "^1[3-9]\\d{9}$".toRegex()
        return regex.matches(phoneNumber)
    }

    fun baixing_isTimeout():Boolean {
        val r = mBaixing_currentTask?.baixing_isTimeOver() ?: true
        if (r) {
            mBaixing_currentTask = null
        }
        return r
    }
}