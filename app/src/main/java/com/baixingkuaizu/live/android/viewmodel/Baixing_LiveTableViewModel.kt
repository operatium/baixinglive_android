package com.baixingkuaizu.live.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_CategoryDataEntity
import com.baixingkuaizu.live.android.http.Baixing_CoreWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

class Baixing_LiveTableViewModel: ViewModel() {

    private val _netError = MutableLiveData<String>()
    val netError: LiveData<String> = _netError

    private val _netTable = MutableLiveData<List<Baixing_CategoryDataEntity>>()
    val netTable: LiveData<List<Baixing_CategoryDataEntity>> = _netTable

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