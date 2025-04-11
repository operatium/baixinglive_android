package com.baixingkuaizu.live.android.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baixingkuaizu.live.android.busiess.task.login.Baixing_LoginTask
import com.baixingkuaizu.live.android.busiess.task.login.Baixing_LoginTaskListener
import com.baixingkuaizu.live.android.busiess.task.login.Baixing_LoginTaskManager
import com.baixingkuaizu.live.android.busiess.task.login.Baixing_SendVerficationCodeTask
import com.baixingkuaizu.live.android.busiess.task.login.Baixing_SendVerficationCodeTaskListener
import com.baixingkuaizu.live.android.busiess.task.login.Baixing_SendVerficationCodeTaskManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

class Baixing_LoginViewModel:ViewModel() {
    val TAG = "yyx类Baixing_LoginViewModel"
    // 用于存储验证码倒计时时间
    private val _mBaixing_codeTime = MutableLiveData<Int>()
    val mBaixing_codeTime: LiveData<Int> = _mBaixing_codeTime

    private val _mBaixing_toast = MutableLiveData<String>()
    val mBaixing_toast: LiveData<String> = _mBaixing_toast

    private var mBaixing_netCode: String? = null

    private val _mBaixing_loginLoading = MutableLiveData<Boolean>()
    val mBaixing_loginLoading: LiveData<Boolean> = _mBaixing_loginLoading

    private val _mBaixing_login = MutableLiveData<Boolean>()
    val mBaixing_login: LiveData<Boolean> = _mBaixing_login

    // 发送验证码的函数
    fun baixing_sendVerificationCode(phoneNumber: String) {
        Baixing_SendVerficationCodeTaskManager.let { manager ->
            if (!manager.baixing_isValidPhoneNumber(phoneNumber)) {
                _mBaixing_toast.value = "手机号码不合法"
                return
            }
            if (!manager.baixing_isTimeout()) {
                _mBaixing_toast.value = "验证码发送过于频繁"
                return
            }
            mBaixing_netCode = null
            viewModelScope.launch(Dispatchers.Default) {
                withTimeoutOrNull(3000) {
                    Baixing_SendVerficationCodeTaskManager.sendVerificationCode(
                        taskName = "发送验证码${Baixing_SendVerficationCodeTaskManager.baixing_obtainID()}",
                        phone = phoneNumber,
                        listener = baixing_obtainSendCodeListener()
                    )
                } ?: run {
                    viewModelScope.launch(Dispatchers.Main) {
                        _mBaixing_toast.value = "验证码发送超时"
                        mBaixing_netCode = null
                        Baixing_SendVerficationCodeTaskManager.baixing_cancelCurrentTask()
                    }
                }
            }
        }
    }

    private fun baixing_obtainSendCodeListener() =
        object : Baixing_SendVerficationCodeTaskListener {

            override fun baixing_onStartTask(task: Baixing_SendVerficationCodeTask) {
                viewModelScope.launch(Dispatchers.Main) {
                    _mBaixing_toast.value = "验证码发送中"
                }
            }

            override fun baixing_onEndTask(task: Baixing_SendVerficationCodeTask) {
                viewModelScope.launch(Dispatchers.Main) {
                    mBaixing_netCode = task.mbaixing_code
                }
            }

            override fun baixing_onTime(
                task: Baixing_SendVerficationCodeTask,
                second: Int
            ) {
                viewModelScope.launch(Dispatchers.Main) {
                    _mBaixing_codeTime.value = second
                }
            }

            override fun baixing_onDestroyTask(task: Baixing_SendVerficationCodeTask) {
                viewModelScope.launch(Dispatchers.Main) {
                    _mBaixing_codeTime.value = -1
                }
            }

            override fun baixing_onCancelTask(task: Baixing_SendVerficationCodeTask) {
                viewModelScope.launch(Dispatchers.Main) {
                    _mBaixing_codeTime.value = -1
                    _mBaixing_toast.value = "取消发送验证码"
                }
            }

        }

    fun baixing_login(appContext: Context, phoneNumber: String, code: String) {
        _mBaixing_loginLoading.value = true
        val loginListener = object : Baixing_LoginTaskListener {

            override fun baixing_onStartTask(task: Baixing_LoginTask) {}

            override fun baixing_onEndTask(task: Baixing_LoginTask) {
                viewModelScope.launch {
                    _mBaixing_loginLoading.value = false
                    _mBaixing_login.value = true
                }
            }

            override fun baixing_onLoginTimeOut(task: Baixing_LoginTask) {
                viewModelScope.launch {
                    _mBaixing_loginLoading.value = false
                    _mBaixing_toast.value = "登录超时"
                }
            }

            override fun baixing_onLoginError(task: Baixing_LoginTask) {
                viewModelScope.launch {
                    _mBaixing_loginLoading.value = false
                    _mBaixing_toast.value = "登录错误"
                }
            }
        }
        viewModelScope.launch(Dispatchers.Default) {
            val token1 = withTimeoutOrNull(1000) {
                Log.d(TAG, "baixing_login: baixing_loginAccount1")
                Baixing_LoginTaskManager.baixing_loginAccount(
                    appContext,
                    phoneNumber,
                    code,
                    loginListener
                )
            } ?: run {
                Log.d(TAG, "baixing_login: baixing_onLoginTimeOut")
                Baixing_LoginTaskManager.baixing_getCurrentTask()?.baixing_onLoginTimeOut()
            }
            Log.d(TAG, "baixing_login: token1: ${token1}")
        }
    }

    fun baixing_checkVerificationCode(code: String): Boolean {
        return mBaixing_netCode == code
    }

    fun baixing_clearNetCode() {
        mBaixing_netCode = null
    }

    fun baixing_obtainCodeTime() {
        if (Baixing_SendVerficationCodeTaskManager.baixing_isTimeout()) {
            _mBaixing_codeTime.value = -1
        }
    }
}
