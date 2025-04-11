package com.baixingkuaizu.live.android.busiess.task


/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 任务基类，所有任务类的父类，提供任务生命周期管理的基本方法
 */
open class Baixing_BaseTask(val taskName:String) {
    override fun toString(): String {
        return "Baixing_BaseTask(taskName='$taskName')"
    }

    open fun baixing_onCreateTask() {

    }

    open fun baixing_onDestroyTask() {

    }

    open fun baixing_onStartTask() {

    }

    open fun baixing_onEndTask() {

    }

    open fun baixing_onStopTask() {

    }

    open fun baixing_onTimeOut() {

    }

    open fun baixing_onError() {

    }
}