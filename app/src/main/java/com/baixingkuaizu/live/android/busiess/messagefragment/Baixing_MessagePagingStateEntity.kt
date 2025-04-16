package com.baixingkuaizu.live.android.busiess.messagefragment

import com.baixingkuaizu.live.android.base.Baixing_Entity

data class Baixing_MessagePagingStateEntity(
    val isLoading: Boolean = false,
    val items: List<Baixing_MessageItemEntity> = emptyList(),
    val error: Throwable? = null,
    val endReached: Boolean = false,
    val page: Int = 1,
    val pageSize: Int = 20
) :Baixing_Entity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Baixing_MessagePagingStateEntity

        if (isLoading != other.isLoading) return false
        if (endReached != other.endReached) return false
        if (page != other.page) return false
        if (pageSize != other.pageSize) return false
        if (items != other.items) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isLoading.hashCode()
        result = 31 * result + endReached.hashCode()
        result = 31 * result + page
        result = 31 * result + pageSize
        result = 31 * result + items.hashCode()
        result = 31 * result + (error?.hashCode() ?: 0)
        return result
    }
}