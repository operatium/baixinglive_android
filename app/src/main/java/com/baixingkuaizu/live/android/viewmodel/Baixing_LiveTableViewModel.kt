package com.baixingkuaizu.live.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_CategoryData
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveDataCache
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveDataEntity
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LivePageEntity
import com.baixingkuaizu.live.android.http.Baixing_CoreWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

class Baixing_LiveTableViewModel: ViewModel() {

    private val _netError = MutableLiveData<String>()
    val netError: LiveData<String> = _netError

    private val _netTable = MutableLiveData<List<Baixing_CategoryData>>()
    val netTable: LiveData<List<Baixing_CategoryData>> = _netTable

    fun requestTable() {
        viewModelScope.launch(Dispatchers.Default) {
            withTimeoutOrNull(5000) {
                Baixing_CoreWork.baixing_liveTabs()
            }?.let {
                withContext(Dispatchers.Main) {
                    _netTable.value = it
                }
                return@launch
            }
            withContext(Dispatchers.Main) {
                _netError.value = "请求超时"
            }
            return@launch
        }
    }
}