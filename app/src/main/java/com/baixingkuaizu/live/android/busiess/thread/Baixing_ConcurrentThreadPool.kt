package com.baixingkuaizu.live.android.busiess.thread

import java.util.concurrent.*

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description:并发线程池
 */
class Baixing_ConcurrentThreadPool private constructor() {
    private val mBaixing_corePoolSize = 8
    private val mBaixing_maximumPoolSize = Int.MAX_VALUE
    private val mBaixing_keepAliveTime = 60L
    private val mBaixing_timeUnit = TimeUnit.SECONDS
    private val mBaixing_workQueue: BlockingQueue<Runnable> = LinkedBlockingQueue(100)
    private val mBaixing_threadFactory = Executors.defaultThreadFactory()
    private val mBaixing_handler = ThreadPoolExecutor.CallerRunsPolicy()

    private val mBaixing_threadPool: ThreadPoolExecutor = ThreadPoolExecutor(
        mBaixing_corePoolSize,
        mBaixing_maximumPoolSize,
        mBaixing_keepAliveTime,
        mBaixing_timeUnit,
        mBaixing_workQueue,
        mBaixing_threadFactory,
        mBaixing_handler
    )

    companion object {
        private var mBaixing_instance: Baixing_ConcurrentThreadPool? = null

        @Synchronized
        fun baixing_getInstance(): Baixing_ConcurrentThreadPool {
            if (mBaixing_instance == null) {
                mBaixing_instance = Baixing_ConcurrentThreadPool()
            }
            return mBaixing_instance!!
        }
    }

    fun baixing_submitTask(task: Baixing_Thread) {
        mBaixing_threadPool.execute(task)
    }

    fun baixing_clear() {
        mBaixing_workQueue.clear()
        mBaixing_threadPool.shutdownNow()
        mBaixing_threadPool.queue.forEach {
            if (it is Baixing_Thread) {
                it.baixing_interrupt()
            }
        }
    }

    fun baixing_interruptTask(taskId: Int) {
        mBaixing_threadPool.queue.forEach {
            if (it is Baixing_Thread && it.baixing_getTaskId() == taskId) {
                it.baixing_interrupt()
                mBaixing_workQueue.remove(it)
                return
            }
        }
    }
}