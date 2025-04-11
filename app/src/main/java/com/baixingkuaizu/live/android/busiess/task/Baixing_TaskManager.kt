package com.baixingkuaizu.live.android.busiess.task

import java.util.concurrent.ConcurrentHashMap

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 任务管理器基类，提供任务的添加、删除、查询和清理等通用功能
 */
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
