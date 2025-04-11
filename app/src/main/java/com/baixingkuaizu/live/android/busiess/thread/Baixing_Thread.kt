package com.baixingkuaizu.live.android.busiess.thread

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 线程任务封装类
 */
open class Baixing_Thread(private val mBaixing_taskId: Int, private val mBaixing_task: Runnable):Runnable {
    private var mBaixing_isInterrupted = false

    fun baixing_interrupt() {
        mBaixing_isInterrupted = true
    }

    fun baixing_getTaskId(): Int {
        return mBaixing_taskId
    }

    fun baixing_run() {
        if (!mBaixing_isInterrupted) {
            mBaixing_task.run()
        }
    }

    override fun run() {
        baixing_run()
    }
}