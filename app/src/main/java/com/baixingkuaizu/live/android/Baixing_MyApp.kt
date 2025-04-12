package com.baixingkuaizu.live.android

import android.app.Application
import android.content.Context

/**
 * @author yuyuexing
 * @date: 2025/4/14
 * @description: 应用程序全局入口类
 */
class Baixing_MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }

    companion object {
        private lateinit var sInstance: Baixing_MyApp

        fun baixing_getInstance(): Baixing_MyApp {
            return sInstance
        }

        fun baixing_getContext(): Context {
            return sInstance.applicationContext
        }
    }
}