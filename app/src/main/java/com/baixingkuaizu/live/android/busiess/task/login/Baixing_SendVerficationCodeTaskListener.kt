package com.baixingkuaizu.live.android.busiess.task.login

interface Baixing_SendVerficationCodeTaskListener {

    fun baixing_onStartTask(task: Baixing_SendVerficationCodeTask)

    fun baixing_onEndTask(task: Baixing_SendVerficationCodeTask)

    fun baixing_onTime(task: Baixing_SendVerficationCodeTask, second:Int)

    fun baixing_onDestroyTask(task: Baixing_SendVerficationCodeTask)

    fun baixing_onCancelTask(task: Baixing_SendVerficationCodeTask)
}