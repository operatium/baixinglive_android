package com.baixingkuaizu.live.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baixingkuaizu.live.android.busiess.searchfragment.Baixing_SearchAnchorEntity
import com.baixingkuaizu.live.android.busiess.searchfragment.Baixing_SearchHistoryManager
import com.baixingkuaizu.live.android.http.Baixing_CoreWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 搜索视图模型，负责处理搜索相关的业务逻辑
 */
class Baixing_SearchViewModel : ViewModel() {
    private val _mBaixing_searchHistory = MutableLiveData<List<String>>()
    val mBaixing_searchHistory: LiveData<List<String>> = _mBaixing_searchHistory

    private val _mBaixing_isLoading = MutableLiveData<Boolean>()
    val mBaixing_isLoading: LiveData<Boolean> = _mBaixing_isLoading

    private val _mBaixing_toasterror = MutableLiveData<String>()
    val mBaixing_toasterror: LiveData<String> = _mBaixing_toasterror

    private val _mBaixing_empty = MutableLiveData<Boolean>()
    val mBaixing_empty: LiveData<Boolean> = _mBaixing_empty

    private val _mBaixing_searchResults = MutableLiveData<List<Baixing_SearchAnchorEntity>>()
    val mBaixing_searchResults: LiveData<List<Baixing_SearchAnchorEntity>> = _mBaixing_searchResults

    fun baixing_search(keyword: String?) {
        if (keyword.isNullOrBlank()) {
            throw Exception("搜索关键词 不能传空")
        }
        _mBaixing_isLoading.value = true
        viewModelScope.launch(Dispatchers.Default) {
            try {
                Baixing_SearchHistoryManager.baixing_addSearchKeyword(keyword)
                withTimeoutOrNull(Baixing_CoreWork.mBaixing_HttpTimeout) {
                    Baixing_CoreWork.baixing_searchAnchor(keyword)
                }?.let {
                    withContext(Dispatchers.Main) {
                        if (it.isNotEmpty()) {
                            _mBaixing_searchResults.value = it
                        } else {
                            _mBaixing_empty.value = true
                        }
                        _mBaixing_isLoading.value = false
                        baixing_loadSearchHistory()
                    }
                    return@launch
                }
                withContext(Dispatchers.Main) {
                    _mBaixing_toasterror.value = "搜索超时"
                    _mBaixing_isLoading.value = false
                }
                return@launch
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _mBaixing_toasterror.value = e.message ?: "搜索失败"
                    _mBaixing_isLoading.value = false
                }
            }
        }
    }

    fun baixing_loadSearchHistory() {
        viewModelScope.launch(Dispatchers.Default) {
            val history = Baixing_SearchHistoryManager.baixing_getSearchHistory()
            withContext(Dispatchers.Main) {
                _mBaixing_searchHistory.value = history
            }
        }
    }

    fun baixing_clearSearchHistory() {
        viewModelScope.launch(Dispatchers.Default) {
            Baixing_SearchHistoryManager.baixing_clearSearchHistory()
            withContext(Dispatchers.Main) {
                _mBaixing_searchHistory.value = emptyList()
            }
        }
    }

    fun baixing_removeSearchKeyword(keyword: String) {
        viewModelScope.launch(Dispatchers.Default) {
            Baixing_SearchHistoryManager.baixing_removeSearchKeyword(keyword)
            val history = Baixing_SearchHistoryManager.baixing_getSearchHistory()
            withContext(Dispatchers.Main) {
                _mBaixing_searchHistory.value = history
            }
        }
    }

    fun baixing_retry() {
        _mBaixing_searchHistory.value?.firstOrNull()?.let { keyword ->
            baixing_search(keyword)
        }
    }
}