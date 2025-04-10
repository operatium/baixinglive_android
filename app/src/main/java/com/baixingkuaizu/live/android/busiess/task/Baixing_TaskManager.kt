package com.baixingkuaizu.live.android.busiess.task

import java.util.concurrent.ConcurrentHashMap

open class Baixing_TaskManager<T: Baixing_BaseTask> {
    private val mBaixing_taskList = ConcurrentHashMap<String, T>()

    fun baixing_addTask(task: T) {
        mBaixing_taskList[task.taskName] = task
    }

    fun baixing_removeTask(task: T) {
        mBaixing_taskList.remove(task.taskName)
    }

    fun baixing_getTaskByName(taskName: String): T? {
        return mBaixing_taskList[taskName]
    }

    fun baixing_getAllTasks(): List<T> {
        return mBaixing_taskList.values.toList()
    }

    fun baixing_clearAllTasks(releaseOpt: ((String, T)->Unit)? = null) {
        if (releaseOpt != null) {
            mBaixing_taskList.forEach { (t, u) ->
                releaseOpt.invoke(t, u)
            }
        }
        mBaixing_taskList.clear()
    }
}
