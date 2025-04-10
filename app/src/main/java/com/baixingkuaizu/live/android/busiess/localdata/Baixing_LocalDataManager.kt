package com.baixingkuaizu.live.android.busiess.localdata

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Baixing_LocalDataManager(private val context: Context) {

    private val mBaixing_sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(BAIXING_PRIVACY_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun baixing_isPrivacyAgreed(): Boolean {
        return mBaixing_sharedPreferences.getBoolean(BAIXING_KEY_PRIVACY_AGREED, false)
    }

    fun baixing_setPrivacyAgreed(agreed: Boolean) {
        mBaixing_sharedPreferences.edit { putBoolean(BAIXING_KEY_PRIVACY_AGREED, agreed) }
    }

    companion object {
        private const val BAIXING_PRIVACY_PREFERENCES = "baixing_privacy_preferences"

        private const val BAIXING_KEY_PRIVACY_AGREED = "baixing_key_privacy_agreed"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var mBaixing_instance: Baixing_LocalDataManager? = null

        fun baixing_getInstance(context: Context): Baixing_LocalDataManager {
            return mBaixing_instance ?: synchronized(this) {
                mBaixing_instance ?: Baixing_LocalDataManager(context.applicationContext).also {
                    mBaixing_instance = it
                }
            }
        }
    }
}