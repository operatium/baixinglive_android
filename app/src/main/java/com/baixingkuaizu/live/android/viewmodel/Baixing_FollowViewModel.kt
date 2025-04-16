package com.baixingkuaizu.live.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baixingkuaizu.live.android.busiess.followfragment.Baixing_FollowDataCache
import com.baixingkuaizu.live.android.busiess.followfragment.Baixing_FollowGirlEntity
import com.baixingkuaizu.live.android.http.Baixing_CoreWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Baixing_FollowViewModel : ViewModel() {
    private val _mBaixing_onlineList = MutableLiveData<List<Baixing_FollowGirlEntity>>()
    val mBaixing_onlineList: LiveData<List<Baixing_FollowGirlEntity>> = _mBaixing_onlineList

    private val _mBaixing_offlineList = MutableLiveData<List<Baixing_FollowGirlEntity>>()
    val mBaixing_offlineList: LiveData<List<Baixing_FollowGirlEntity>> = _mBaixing_offlineList

    private val _mBaixing_isLoading = MutableLiveData<Boolean>()
    val mBaixing_isLoading: LiveData<Boolean> = _mBaixing_isLoading

    private val _mBaixing_error = MutableLiveData<String?>()
    val mBaixing_error: LiveData<String?> = _mBaixing_error

    fun baixing_requestFollowList() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                withContext(Dispatchers.Main) {
                    _mBaixing_isLoading.value = true
                    _mBaixing_error.value = null
                }

                val followList = Baixing_CoreWork.baixing_followList()
                Baixing_FollowDataCache.baixing_updateFollowList(followList)

                withContext(Dispatchers.Main) {
                    _mBaixing_onlineList.value = Baixing_FollowDataCache.baixing_getOnlineList()
                    _mBaixing_offlineList.value = Baixing_FollowDataCache.baixing_getOfflineList()
                    _mBaixing_isLoading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _mBaixing_error.value = e.message ?: "加载失败"
                    _mBaixing_isLoading.value = false
                }
            }
        }
    }

    fun baixing_retry() {
        baixing_requestFollowList()
    }
}