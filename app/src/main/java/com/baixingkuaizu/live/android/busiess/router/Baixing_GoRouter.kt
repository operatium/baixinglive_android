package com.baixingkuaizu.live.android.busiess.router

import android.content.Context
import android.content.Intent
import com.baixingkuaizu.live.android.activity.*
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import java.util.Stack

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 路由管理器，负责处理应用内页面跳转和Activity生命周期管理
 */
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

    fun baixing_jumpLoginActivity() {
        val currentActivity = mBaixing_activityList.peek()
        val intent = Intent(currentActivity, Baixing_LoginActivity::class.java)
        currentActivity.startActivity(intent)
    }

    fun baixing_jumpHomeActivity() {
        val currentActivity = mBaixing_activityList.peek()
        val intent = Intent(currentActivity, Baixing_MainActivity::class.java)
        currentActivity.startActivity(intent)
    }

    fun baixing_jumpTeenModeActivity() {
        val currentActivity = mBaixing_activityList.peek()
        val intent = Intent(currentActivity, Baixing_TeenModeActivity::class.java)
        currentActivity.startActivity(intent)
    }

    fun baixing_callOnCreate(activity: Baixing_BaseActivity) {
        mBaixing_activityList.push(activity)
    }

    fun baixing_callOnDestroy(activity: Baixing_BaseActivity) {
        mBaixing_activityList.remove(activity)
    }
}