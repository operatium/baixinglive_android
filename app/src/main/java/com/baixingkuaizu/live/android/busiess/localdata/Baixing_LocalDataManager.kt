package com.baixingkuaizu.live.android.busiess.localdata

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.baixingkuaizu.live.android.Baixing_MyApp

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
    
    fun baixing_getTokenExpireTime(): Long {
        return mBaixing_sharedPreferences.getLong(BAIXING_KEY_TOKEN_EXPIRE_TIME, 0)
    }
    
    fun baixing_setTokenExpireTime(expireTime: Long) {
        mBaixing_sharedPreferences.edit { putLong(BAIXING_KEY_TOKEN_EXPIRE_TIME, expireTime) }
    }
    
    fun baixing_isTokenValid(): Boolean {
        val token = baixing_getLoginToken()
        val expireTime = baixing_getTokenExpireTime()
        val currentTime = System.currentTimeMillis()
        
        return token.isNotEmpty() && expireTime > currentTime
    }
    
    /**
     * 检查青少年模式对话框是否在24小时内已显示过
     */
    fun baixing_isTeenModeDialogShown(): Boolean {
        val lastShowTime = mBaixing_sharedPreferences.getLong(BAIXING_KEY_TEEN_MODE_DIALOG_SHOW_TIME, 0)
        val currentTime = System.currentTimeMillis()
        val oneDayMillis = 24 * 60 * 60 * 1000L // 24小时的毫秒数
        
        // 如果距离上次显示不足24小时，则认为已显示
        return (currentTime - lastShowTime) < oneDayMillis
    }
    
    /**
     * 记录青少年模式对话框显示时间
     */
    fun baixing_setTeenModeDialogShown() {
        mBaixing_sharedPreferences.edit { 
            putLong(BAIXING_KEY_TEEN_MODE_DIALOG_SHOW_TIME, System.currentTimeMillis()) 
        }
    }
    
    fun baixing_setParentPassword(password: String) {
        mBaixing_sharedPreferences.edit { putString(BAIXING_KEY_PARENT_PASSWORD, password) }
    }
    
    fun baixing_getParentPassword(): String {
        return mBaixing_sharedPreferences.getString(BAIXING_KEY_PARENT_PASSWORD, "") ?: ""
    }
    
    fun baixing_setTeenModeEnabled(enabled: Boolean) {
        mBaixing_sharedPreferences.edit { putBoolean(BAIXING_KEY_TEEN_MODE_ENABLED, enabled) }
    }
    
    fun baixing_isTeenModeEnabled(): Boolean {
        return mBaixing_sharedPreferences.getBoolean(BAIXING_KEY_TEEN_MODE_ENABLED, false)
    }
    
    fun baixing_setLastUsedTime(time: Long) {
        mBaixing_sharedPreferences.edit { putLong(BAIXING_KEY_LAST_USED_TIME, time) }
    }
    
    fun baixing_getLastUsedTime(): Long {
        return mBaixing_sharedPreferences.getLong(BAIXING_KEY_LAST_USED_TIME, 0)
    }
    
    fun baixing_setTodayUsedDuration(duration: Long) {
        mBaixing_sharedPreferences.edit { putLong(BAIXING_KEY_TODAY_USED_DURATION, duration) }
    }
    
    fun baixing_getTodayUsedDuration(): Long {
        return mBaixing_sharedPreferences.getLong(BAIXING_KEY_TODAY_USED_DURATION, 0)
    }
    
    /**
     * 记录最后验证密码的时间
     */
    fun baixing_setLastVerifiedTime(time: Long) {
        mBaixing_sharedPreferences.edit { putLong(BAIXING_KEY_LAST_VERIFIED_TIME, time) }
    }
    
    /**
     * 获取最后验证密码的时间
     */
    fun baixing_getLastVerifiedTime(): Long {
        return mBaixing_sharedPreferences.getLong(BAIXING_KEY_LAST_VERIFIED_TIME, 0)
    }

    companion object {
        private const val BAIXING_PRIVACY_PREFERENCES = "baixing_privacy_preferences"

        private const val BAIXING_KEY_PRIVACY_AGREED = "baixing_key_privacy_agreed"
        private const val BAIXING_KEY_LOGIN_TOKEN = "baixing_key_login_token"
        private const val BAIXING_KEY_TOKEN_EXPIRE_TIME = "baixing_key_token_expire_time"
        private const val BAIXING_KEY_TEEN_MODE_DIALOG_SHOW_TIME = "baixing_key_teen_mode_dialog_show_time"
        private const val BAIXING_KEY_PARENT_PASSWORD = "baixing_key_parent_password"
        private const val BAIXING_KEY_TEEN_MODE_ENABLED = "baixing_key_teen_mode_enabled"
        private const val BAIXING_KEY_LAST_USED_TIME = "baixing_key_last_used_time"
        private const val BAIXING_KEY_TODAY_USED_DURATION = "baixing_key_today_used_duration"
        private const val BAIXING_KEY_LAST_VERIFIED_TIME = "baixing_key_last_verified_time"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var mBaixing_instance: Baixing_LocalDataManager? = null

        fun getInstance(): Baixing_LocalDataManager {
            return mBaixing_instance ?: synchronized(this) {
                mBaixing_instance ?: Baixing_LocalDataManager(Baixing_MyApp.baixing_getContext()).also {
                    mBaixing_instance = it
                }
            }
        }
    }
}