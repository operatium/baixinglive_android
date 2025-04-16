package com.baixingkuaizu.live.android.busiess.searchfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baixingkuaizu.live.android.http.Baixing_CoreWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 搜索视图模型，负责处理搜索相关的业务逻辑
 */
class Baixing_SearchViewModel : ViewModel() {
    private val _mBaixing_searchResults = MutableLiveData<List<Baixing_SearchAnchorEntity>>()
    val mBaixing_searchResults: LiveData<List<Baixing_SearchAnchorEntity>> = _mBaixing_searchResults

    private val _mBaixing_searchHistory = MutableLiveData<List<String>>()
    val mBaixing_searchHistory: LiveData<List<String>> = _mBaixing_searchHistory

    private val _mBaixing_isLoading = MutableLiveData<Boolean>(false)
    val mBaixing_isLoading: LiveData<Boolean> = _mBaixing_isLoading

    private val _mBaixing_error = MutableLiveData<String?>(null)
    val mBaixing_error: LiveData<String?> = _mBaixing_error

    init {
        baixing_loadSearchHistory()
    }

    fun baixing_search(keyword: String) {
        if (keyword.isBlank()) {
            _mBaixing_searchResults.value = emptyList()
            return
        }

        viewModelScope.launch(Dispatchers.Default) {
            try {
                withContext(Dispatchers.Main) {
                    _mBaixing_isLoading.value = true
                    _mBaixing_error.value = null
                }

                Baixing_SearchHistoryManager.baixing_addSearchKeyword(keyword)

                val results = Baixing_CoreWork.baixing_searchAnchor(keyword)

                withContext(Dispatchers.Main) {
                    _mBaixing_searchResults.value = results
                    _mBaixing_isLoading.value = false
                    baixing_loadSearchHistory()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _mBaixing_error.value = e.message ?: "搜索失败"
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
            baixing_loadSearchHistory()
        }
    }

    fun baixing_retry() {
        _mBaixing_searchResults.value?.let {
            if (it.isNotEmpty()) {
                _mBaixing_searchHistory.value?.firstOrNull()?.let { keyword ->
                    baixing_search(keyword)
                }
            }
        }
    }
}