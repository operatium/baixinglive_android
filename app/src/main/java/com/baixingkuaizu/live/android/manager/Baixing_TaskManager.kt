package com.baixingkuaizu.live.android.manager

import com.baixingkuaizu.live.android.base.Baixing_BaseTask

class Baixing_TaskManager private constructor() {
    private val mBaixing_taskList = ArrayList<Baixing_BaseTask>()

    companion object {
        @Volatile
        private var instance: Baixing_TaskManager? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Baixing_TaskManager().also { instance = it }
        }
    }

    fun baixing_addTask(task: Baixing_BaseTask) {
        synchronized(mBaixing_taskList) {
            mBaixing_taskList.add(task)
        }
    }

    fun baixing_removeTask(task: Baixing_BaseTask) {
        synchronized(mBaixing_taskList) {
            mBaixing_taskList.remove(task)
        }
    }

    fun baixing_getTaskByName(taskName: String): Baixing_BaseTask? {
        synchronized(mBaixing_taskList) {
            return mBaixing_taskList.find { it.taskName == taskName }
        }
    }

    fun baixing_getAllTasks(): List<Baixing_BaseTask> {
        synchronized(mBaixing_taskList) {
            return mBaixing_taskList.toList()
        }
    }

    fun baixing_clearAllTasks() {
        synchronized(mBaixing_taskList) {
            mBaixing_taskList.clear()
        }
    }
}
