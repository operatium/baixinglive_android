package com.baixingkuaizu.live.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baixingkuaizu.live.android.busiess.messagefragment.Baixing_MessageDataCache
import com.baixingkuaizu.live.android.busiess.messagefragment.Baixing_MessagePagingStateEntity
import com.baixingkuaizu.live.android.http.Baixing_CoreWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author yuyuexing
 * @since 2025/4/18
 * @description: 消息页面ViewModel，实现MVVM架构
 */
class Baixing_MessageViewModel : ViewModel() {

    private val _mBaixing_messageState = MutableStateFlow(Baixing_MessagePagingStateEntity())
    val mBaixing_messageState: StateFlow<Baixing_MessagePagingStateEntity> = _mBaixing_messageState

    fun baixing_requestMessageUserList(isRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                if (isRefresh) {
                    _mBaixing_messageState.update { it.copy(
                        isLoading = true,
                        error = null,
                        page = 1,
                        items = emptyList()
                    ) }
                } else {
                    _mBaixing_messageState.update { it.copy(
                        isLoading = true,
                        error = null
                    ) }
                }

                val messages = Baixing_CoreWork.baixing_messageUserList()
                Baixing_MessageDataCache.addMessages(messages)
                
                val currentState = _mBaixing_messageState.value
                val newPage = if (isRefresh) 1 else currentState.page
                val newItems = Baixing_MessageDataCache.getUserList(newPage, currentState.pageSize)
                
                withContext(Dispatchers.Main) {
                    _mBaixing_messageState.update { it.copy(
                        isLoading = false,
                        items = if (isRefresh) newItems else it.items + newItems,
                        error = null,
                        endReached = newItems.size < it.pageSize,
                        page = if (newItems.isEmpty()) it.page else it.page + 1
                    ) }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _mBaixing_messageState.update { it.copy(
                        isLoading = false,
                        error = e
                    ) }
                }
            }
        }
    }

    fun baixing_retryLastRequest() {
        val currentState = _mBaixing_messageState.value
        if (currentState.error != null) {
            baixing_requestMessageUserList(currentState.items.isEmpty())
        }
    }

    fun baixing_loadNextPage() {
        val currentState = _mBaixing_messageState.value
        if (!currentState.isLoading && !currentState.endReached && currentState.error == null) {
            baixing_requestMessageUserList(false)
        }
    }
}