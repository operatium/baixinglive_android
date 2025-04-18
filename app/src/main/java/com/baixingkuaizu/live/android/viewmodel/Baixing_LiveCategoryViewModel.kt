package com.baixingkuaizu.live.android.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_GirlDataCache
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_GirlDataEntity
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LivePageEntity
import com.baixingkuaizu.live.android.http.Baixing_CoreWork
import com.baixingkuaizu.live.android.http.Baixing_CoreWork.mBaixing_HttpTimeout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

class Baixing_LiveCategoryViewModel: ViewModel() {

    private val _liveList = MutableLiveData<List<Baixing_GirlDataEntity>>()
    val liveList: LiveData<List<Baixing_GirlDataEntity>> = _liveList

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
    var mBaixing_id = ""
    val TAG = "yyx-Baixing_LiveCategoryViewModel"

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: $mBaixing_id")
    }

    fun requestFirstPage(id: String) {
        mBaixing_id = id
        _retry.value = false
        _hasNextData.value = true
        mBaixing_Page = 0
        _listloading.value = true
        viewModelScope.launch {
            delay(100)
            Baixing_GirlDataCache.getListById(id).let {
                if (it.isNotEmpty()) {
                    _liveList.value = it
                    _listloading.value = false
                } else {
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
                            Baixing_GirlDataCache.clear(id)
                            Baixing_GirlDataCache.addList(
                                id,
                                Baixing_LivePageEntity(id, page, data)
                            )
                            _liveList.value = Baixing_GirlDataCache.getListById(id)
                        }
                    )
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
                Baixing_GirlDataCache.clear(id)
                _listEmpty.value = true
            },
            {
                _netError.value = it
            },
            { page, data ->
                Baixing_GirlDataCache.clear(id)
                Baixing_GirlDataCache.addList(id, Baixing_LivePageEntity(id, page, data))
                _liveList.value = Baixing_GirlDataCache.getListById(id)
                _hasNextData.value = true
            }
        )
    }

    fun requestNextPage(id: String) {
        mBaixing_Page = Baixing_GirlDataCache.incrementPage(id)
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
                Baixing_GirlDataCache.addList(id, Baixing_LivePageEntity(id, page, data))
                _liveList.value = Baixing_GirlDataCache.getListById(id)
            }
        )
    }

    private fun requestList(id:String,
                            page:Int,
                            end: () -> Unit,
                            empty: () -> Unit,
                            error: (String) -> Unit,
                            success: (Int, ArrayList<Baixing_GirlDataEntity>) -> Unit) {
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