package com.baixingkuaizu.live.android.busiess.messagefragment

import android.content.Context
import com.google.gson.Gson
import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap

/**
 * @author yuyuexing
 * @since 2025/4/18
 * @description: 聊天数据缓存类
 */
object Baixing_MessageDataCache {
    private val mBaixing_gson = Gson()
    private val mBaixing_messages = ConcurrentHashMap<String, LinkedList<Baixing_MessageItemEntity>>()

    fun addMessages(messages: List<Baixing_MessageItemEntity>) {
        for (item in messages) {
            if (!mBaixing_messages.contains(item.baixing_id)) {
                val list = LinkedList<Baixing_MessageItemEntity>()
                mBaixing_messages[item.baixing_id] = list
            }
            mBaixing_messages[item.baixing_id]!!.let { list ->
                list.add(item)
                mBaixing_messages[item.baixing_id] = LinkedList(list.sortedBy { it.baixing_timestamp })
            }
        }
    }

    fun getMessageById(id: String): LinkedList<Baixing_MessageItemEntity> {
        if (mBaixing_messages.containsKey(id) && mBaixing_messages[id]!!.isNotEmpty()) {
            val list = mBaixing_messages[id]!!.sortedBy { it.baixing_timestamp }
            return LinkedList(list)
        } else {
            return LinkedList()
        }
    }

    fun getUserList(page: Int = 1, pageSize: Int = 20): List<Baixing_MessageItemEntity> {
        val result = LinkedList<Baixing_MessageItemEntity>()
        for (id in mBaixing_messages.keys) {
            mBaixing_messages[id]!!.maxByOrNull { it.baixing_timestamp }?.let {
                result.add(it)
            }
        }
        val sortedList = result.sortedByDescending { it.baixing_timestamp }
        val startIndex = (page - 1) * pageSize
        return if (startIndex < sortedList.size) {
            sortedList.subList(startIndex, minOf(startIndex + pageSize, sortedList.size))
        } else {
            emptyList()
        }
    }
    
    fun clearCache() {
        mBaixing_messages.clear()
    }
}