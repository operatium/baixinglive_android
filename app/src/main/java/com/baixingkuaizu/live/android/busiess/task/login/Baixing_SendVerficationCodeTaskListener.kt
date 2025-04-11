package com.baixingkuaizu.live.android.busiess.task.login

interface Baixing_SendVerficationCodeTaskListener {

    fun baixing_onCreateTask(task: Baixing_SendVerficationCodeTask)

    fun baixing_onStartTask(task: Baixing_SendVerficationCodeTask)

    fun baixing_onStopTask(task: Baixing_SendVerficationCodeTask)

    fun baixing_onTime(task: Baixing_SendVerficationCodeTask, second:Int)

    fun baixing_onDestroyTask(task: Baixing_SendVerficationCodeTask)
}