package com.baixingkuaizu.live.android.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.baixingkuaizu.live.android.busiess.router.Baixing_GoRouter

open class Baixing_BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Baixing_GoRouter.baixing_callOnCreate(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Baixing_GoRouter.baixing_callOnDestroy(this)
    }
}