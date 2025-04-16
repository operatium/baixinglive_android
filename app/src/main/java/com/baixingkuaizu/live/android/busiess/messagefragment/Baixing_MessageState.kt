package com.baixingkuaizu.live.android.busiess.messagefragment

sealed class Baixing_MessageState<out T> {
    object Loading : Baixing_MessageState<Nothing>()
    data class Success<T>(val data: T) : Baixing_MessageState<T>()
    data class Error(val error: Throwable) : Baixing_MessageState<Nothing>()
}

data class Baixing_MessagePagingState(
    val isLoading: Boolean = false,
    val items: List<Baixing_MessageItemEntity> = emptyList(),
    val error: Throwable? = null,
    val endReached: Boolean = false,
    val page: Int = 1,
    val pageSize: Int = 20
) 