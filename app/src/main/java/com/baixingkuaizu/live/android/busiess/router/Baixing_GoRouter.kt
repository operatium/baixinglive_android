package com.baixingkuaizu.live.android.busiess.router

import android.content.Intent
import com.baixingkuaizu.live.android.activity.*
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import java.util.Stack

object Baixing_GoRouter {
    private val mBaixing_activityList = Stack<Baixing_BaseActivity>()

    fun baixing_jumpWebActivity(
        name: String = "",
        url: String = "",
        taskName: String = "",
        taskType: String = ""
    ) {
        val currentActivity = mBaixing_activityList.peek()
        val intent = Intent(currentActivity, Baixing_WebActivity::class.java)
        intent.putExtra("name", name)
        intent.putExtra("url", url)
        intent.putExtra("task name", taskName)
        intent.putExtra("task type", taskType)
        currentActivity.startActivity(intent)
    }

    fun baixing_callOnCreate(activity: Baixing_BaseActivity) {
        mBaixing_activityList.push(activity)
    }

    fun baixing_callOnDestroy(activity: Baixing_BaseActivity) {
        mBaixing_activityList.remove(activity)
    }
}