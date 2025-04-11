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

    suspend fun sendVerificationCode(taskName:String, phone:String, listener: Baixing_SendVerficationCodeTaskListener):String {
        Baixing_SendVerficationCodeTask(taskName, phone).run {
            mBaixing_currentTask = this
            addListener("self", listener)
            addListener("global", baixing_obtainSendCodeListener())
            return baixing_sendVerificationCode()
        }
    }

    private fun baixing_obtainSendCodeListener(): Baixing_SendVerficationCodeTaskListener = object : Baixing_SendVerficationCodeTaskListener {
        override fun baixing_onStartTask(task: Baixing_SendVerficationCodeTask) {
        }

        override fun baixing_onEndTask(task: Baixing_SendVerficationCodeTask) {
        }

        override fun baixing_onTime(task: Baixing_SendVerficationCodeTask, second: Int) {
        }

        override fun baixing_onDestroyTask(task: Baixing_SendVerficationCodeTask) {
            if (mBaixing_currentTask == task) {
                mBaixing_currentTask = null
            }
        }

        override fun baixing_onCancelTask(task: Baixing_SendVerficationCodeTask) {
            if (mBaixing_currentTask == task) {
                mBaixing_currentTask = null
            }
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

    fun baixing_cancelCurrentTask() {
        mBaixing_currentTask?.baixing_cancel()
        mBaixing_currentTask = null
    }
}