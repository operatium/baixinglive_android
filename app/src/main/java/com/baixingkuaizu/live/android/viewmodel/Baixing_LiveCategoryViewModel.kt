package com.baixingkuaizu.live.android.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveDataCache
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveDataEntity
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LivePageEntity
import com.baixingkuaizu.live.android.http.Baixing_CoreWork
import com.baixingkuaizu.live.android.http.Baixing_CoreWork.mBaixing_HttpTimeout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

class Baixing_LiveCategoryViewModel: ViewModel() {

    private val _liveList = MutableLiveData<List<Baixing_LiveDataEntity>>()
    val liveList: LiveData<List<Baixing_LiveDataEntity>> = _liveList

    private val _hasNextData = MutableLiveData<Boolean>()
    val hasNextData: LiveData<Boolean> = _hasNextData

    private val _netError = MutableLiveData<String>()
    val netError: LiveData<String> = _netError

    private val _listloading = MutableLiveData<Boolean>()
    val listloading: LiveData<Boolean> = _listloading

    private val _listrefersh = MutableLiveData<Boolean>()
    val listrefersh: LiveData<Boolean> = _listrefersh

    private val _listfoot = MutableLiveData<Boolean>()
    val listfoot: LiveData<Boolean> = _listfoot

    private val _listEmpty = MutableLiveData<Boolean>()
    val listEmpty: LiveData<Boolean> = _listEmpty

    private val _retry = MutableLiveData<Boolean>()
    val retry: LiveData<Boolean> = _retry

    var mBaixing_Page = 0

    fun requestFirstPage(id: String) {
        _retry.value = false
        _hasNextData.value = true
        mBaixing_Page = 0
        _listloading.value = true
        requestList(
            id,
            mBaixing_Page,
            {
                _listloading.value = false
            },
            {
                _listEmpty.value = true
            },
            {
                _netError.value = it
                _retry.value = true
            },
            { page, data ->
                Baixing_LiveDataCache.clear(id)
                Baixing_LiveDataCache.addList(id, Baixing_LivePageEntity(id, page, data))
                _liveList.value = Baixing_LiveDataCache.getListById(id)
            }
        )
        viewModelScope.launch {
            delay(100)
            Baixing_LiveDataCache.getListById(id).let {
                if (it.isNotEmpty()) {
                    _liveList.value = it
                }
            }
        }
    }

    fun requestRefersh(id: String) {
        _retry.value = false
        mBaixing_Page = 0
        _listrefersh.value = true
        requestList(
            id,
            mBaixing_Page,
            {
                _listrefersh.value = false
            },
            {
                Baixing_LiveDataCache.clear(id)
                _listEmpty.value = true
            },
            {
                _netError.value = it
            },
            { page, data ->
                Baixing_LiveDataCache.clear(id)
                Baixing_LiveDataCache.addList(id, Baixing_LivePageEntity(id, page, data))
                _liveList.value = Baixing_LiveDataCache.getListById(id)
                _hasNextData.value = true
            }
        )
    }

    fun requestNextPage(id: String) {
        mBaixing_Page = Baixing_LiveDataCache.incrementPage(id)
        Log.d("yyx", "requestNextPage: $id $mBaixing_Page")
        _listfoot.value = true
        requestList(
            id,
            mBaixing_Page,
            {
                _listfoot.value = false
            },
            {
                _hasNextData.value = false

            },
            {
            },
            { page, data ->
                Baixing_LiveDataCache.addList(id, Baixing_LivePageEntity(id, page, data))
                _liveList.value = Baixing_LiveDataCache.getListById(id)
            }
        )
    }

    private fun requestList(id:String,
                            page:Int,
                            end: () -> Unit,
                            empty: () -> Unit,
                            error: (String) -> Unit,
                            success: (Int, ArrayList<Baixing_LiveDataEntity>) -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            withTimeoutOrNull(mBaixing_HttpTimeout) {
                Baixing_CoreWork.baixing_liveList(id, page)
            }?.let {
                if (it.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        empty.invoke()
                        end.invoke()
                    }
                    return@launch
                } else {
                    withContext(Dispatchers.Main) {
                        success.invoke(page, it)
                        end.invoke()
                    }
                    return@launch
                }
            }
            withContext(Dispatchers.Main) {
                error.invoke("网络超时")
                end.invoke()
            }
            return@launch
        }
    }
}