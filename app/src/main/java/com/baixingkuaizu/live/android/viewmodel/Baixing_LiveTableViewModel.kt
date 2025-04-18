package com.baixingkuaizu.live.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_CategoryDataEntity
import com.baixingkuaizu.live.android.http.Baixing_CoreWork
import com.baixingkuaizu.live.android.http.Baixing_CoreWork.mBaixing_HttpTimeout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

class Baixing_LiveTableViewModel: ViewModel() {

    private val _netError = MutableLiveData<String>()
    val netError: LiveData<String> = _netError

    private val _netTable = MutableLiveData<List<Baixing_CategoryDataEntity>>()
    val netTable: LiveData<List<Baixing_CategoryDataEntity>> = _netTable

    private val _netEmpty = MutableLiveData<Boolean>()
    val netEmpty: LiveData<Boolean> = _netEmpty

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean> = _viewLoading

    fun requestTable() {
        _viewLoading.value = true
        viewModelScope.launch(Dispatchers.Default) {
            withTimeoutOrNull(mBaixing_HttpTimeout) {
                Baixing_CoreWork.baixing_liveTabs()
            }?.let {
                withContext(Dispatchers.Main) {
                    if (it.isNotEmpty()) {
                        _netTable.value = it
                    } else {
                        _netEmpty.value = true
                    }
                    _viewLoading.value = false
                }
                return@launch
            }
            withContext(Dispatchers.Main) {
                _netError.value = "请求超时"
                _viewLoading.value = false
            }
            return@launch
        }
    }
}