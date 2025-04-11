package com.baixingkuaizu.live.android.busiess.localdata

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 本地数据管理器，负责处理应用本地数据的存储和读取，如隐私政策同意状态
 */
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

    fun baixing_getLoginToken(): String {
        return mBaixing_sharedPreferences.getString(BAIXING_KEY_LOGIN_TOKEN, "") ?: ""
    }

    fun baixing_setLoginToken(token: String) {
        mBaixing_sharedPreferences.edit { putString(BAIXING_KEY_LOGIN_TOKEN, token) }
    }

    fun baixing_clearLoginToken() {
        mBaixing_sharedPreferences.edit { remove(BAIXING_KEY_LOGIN_TOKEN) }
    }

    companion object {
        private const val BAIXING_PRIVACY_PREFERENCES = "baixing_privacy_preferences"

        private const val BAIXING_KEY_PRIVACY_AGREED = "baixing_key_privacy_agreed"
        private const val BAIXING_KEY_LOGIN_TOKEN = "baixing_key_login_token"

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