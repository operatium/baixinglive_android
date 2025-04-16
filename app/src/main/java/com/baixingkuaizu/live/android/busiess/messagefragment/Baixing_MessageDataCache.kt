package com.baixingkuaizu.live.android.busiess.messagefragment

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap

/**
 * @author yuyuexing
 * @since 2025/4/18
 * @description: 聊天数据缓存类
 */
object Baixing_MessageDataCache {
    private const val PREF_NAME = "baixing_message_cache"
    private const val KEY_MESSAGES = "messages"
    
    private lateinit var mBaixing_preferences: SharedPreferences
    private val mBaixing_gson = Gson()
    private val mBaixing_messages = ConcurrentHashMap<String, LinkedList<Baixing_MessageItemEntity>>()
    
    fun baixing_init(context: Context) {
        mBaixing_preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        baixing_loadFromDisk()
    }
    
    private fun baixing_loadFromDisk() {
        val json = mBaixing_preferences.getString(KEY_MESSAGES, null)
        if (json != null) {
            val type = object : TypeToken<Map<String, LinkedList<Baixing_MessageItemEntity>>>() {}.type
            val loadedMessages = mBaixing_gson.fromJson<Map<String, LinkedList<Baixing_MessageItemEntity>>>(json, type)
            mBaixing_messages.putAll(loadedMessages)
        }
    }
    
    private fun baixing_saveToDisk() {
        val json = mBaixing_gson.toJson(mBaixing_messages)
        mBaixing_preferences.edit().putString(KEY_MESSAGES, json).apply()
    }

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
        baixing_saveToDisk()
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
        baixing_saveToDisk()
    }
}