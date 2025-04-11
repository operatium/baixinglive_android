package com.baixingkuaizu.live.android.busiess.task.login

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 登录任务监听器接口，定义登录任务的生命周期回调方法
 */
interface Baixing_LoginTaskListener {
    /**
     * 当登录任务创建时调用
     */
    fun baixing_onCreateTask(task: Baixing_LoginTask)

    /**
     * 当开始登录任务时调用
     */
    fun baixing_onStartTask(task: Baixing_LoginTask)

    /**
     * 当登录任务结束时调用
     */
    fun baixing_onEndTask(task: Baixing_LoginTask)

    /**
     * 当停止登录任务时调用
     */
    fun baixing_onStopTask(task: Baixing_LoginTask)

    /**
     * 当销毁登录任务时调用
     */
    fun baixing_onDestroyTask(task: Baixing_LoginTask)
}