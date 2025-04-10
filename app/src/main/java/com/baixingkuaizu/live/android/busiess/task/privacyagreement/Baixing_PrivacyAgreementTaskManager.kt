package com.baixingkuaizu.live.android.busiess.task.privacyagreement

import android.content.Context
import android.webkit.WebView
import android.widget.FrameLayout
import com.baixingkuaizu.live.android.busiess.task.Baixing_TaskManager
import com.baixingkuaizu.live.android.widget.web.Baixing_WebViewWrapper

object Baixing_PrivacyAgreementTaskManager : Baixing_TaskManager<Baixing_PrivacyAgreementTask>() {

    fun createTask(context: Context) {
        baixing_addTask(Baixing_PrivacyAgreementTask("隐私政策", "https://www.2339.com"))
        baixing_addTask(Baixing_PrivacyAgreementTask("用户协议", "https://www.163.com"))
        baixing_getAllTasks().forEach {
            it.baixing_preLoad(context.applicationContext)
        }
    }

    fun destroyTask() {
        baixing_clearAllTasks { _, task ->
            task.baixing_destroy()
        }
    }

    fun show(taskName:String, frameLayout: FrameLayout):Baixing_WebViewWrapper? {
        return baixing_getTaskByName(taskName)?.baixing_show(frameLayout)
    }
}
