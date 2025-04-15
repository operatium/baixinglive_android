package com.baixingkuaizu.live.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveDataCache
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveDataEntity
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LivePageEntity
import com.baixingkuaizu.live.android.http.Baixing_CoreWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

class Baixing_LiveCategoryViewModel: ViewModel() {

    private val _liveList = MutableLiveData<List<Baixing_LiveDataEntity>>()
    val liveList: LiveData<List<Baixing_LiveDataEntity>> = _liveList

    private val _isListBottom = MutableLiveData<Boolean>()
    val isListBottom: LiveData<Boolean> = _isListBottom

    private val _netError = MutableLiveData<String>()
    val netError: LiveData<String> = _netError

    private val _listloading = MutableLiveData<Boolean>()
    val listloading: LiveData<Boolean> = _listloading

    var mBaixing_Page = 0

    fun requestFirstPage(id:String, listLoading: Boolean = true) {
        mBaixing_Page = 0
        _listloading.value = listLoading
        requestList(id)
        viewModelScope.launch {
            delay(100)
            Baixing_LiveDataCache.getListById(id).let {
                if (it.isNotEmpty()) {
                    _liveList.value = it
                    _isListBottom.value = false
                    _listloading.value = false
                }
            }
        }
    }

    fun requestNextPage(id:String, listLoading: Boolean = true) {
        mBaixing_Page = Baixing_LiveDataCache.incrementPage(id)
        _listloading.value = listLoading
        requestList(id, mBaixing_Page)
    }

    private fun requestList(id:String, page:Int = 0) {
        viewModelScope.launch(Dispatchers.Default) {
            withTimeoutOrNull(5000) {
                Baixing_CoreWork.baixing_liveList(id, page)
            }?.let {
                if (it.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        _isListBottom.value = true
                        _listloading.value = false
                    }
                    return@launch
                } else {
                    Baixing_LiveDataCache.addList(id, Baixing_LivePageEntity(id, page, it))
                    withContext(Dispatchers.Main) {
                        _liveList.value = Baixing_LiveDataCache.getListById(id)
                        _isListBottom.value = false
                        _listloading.value = false
                    }
                    return@launch
                }
            }
            withContext(Dispatchers.Main) {
                _netError.value = "网络超时"
                _listloading.value = false
            }
            return@launch
        }
    }
}