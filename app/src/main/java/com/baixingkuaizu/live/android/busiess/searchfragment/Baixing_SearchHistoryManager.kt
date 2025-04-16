package com.baixingkuaizu.live.android.busiess.searchfragment

import android.annotation.SuppressLint
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 搜索历史管理类，负责管理搜索历史记录的添加、获取和清除
 */
object Baixing_SearchHistoryManager {
    @SuppressLint("StaticFieldLeak")
    private val mBaixing_LocMer = Baixing_LocalDataManager.getInstance()
    private val mBaixing_gson = Gson()
    private val mBaixing_maxHistorySize = 10

    fun baixing_addSearchKeyword(keyword: String) {
        if (keyword.isBlank()) return
        val historyList = baixing_getSearchHistory().toMutableList()
        historyList.remove(keyword)
        historyList.add(0, keyword)
        if (historyList.size > mBaixing_maxHistorySize) {
            historyList.removeAt(historyList.size - 1)
        }
        baixing_saveHistoryList(historyList)
    }

    fun baixing_getSearchHistory(): List<String> {
        val historyJson = mBaixing_LocMer.baixing_getSearchHistory()
        if (historyJson.isBlank()) return emptyList()
        return baixing_fromJson(historyJson)
    }

    fun baixing_clearSearchHistory() {
        mBaixing_LocMer.baixing_setSearchHistory("")
    }

    fun baixing_removeSearchKeyword(keyword: String) {
        val historyList = baixing_getSearchHistory().toMutableList()
        historyList.remove(keyword)
        baixing_saveHistoryList(historyList)
    }

    private fun baixing_saveHistoryList(historyList: List<String>) {
        try {
            val historyJson = mBaixing_gson.toJson(historyList)
            mBaixing_LocMer.baixing_setSearchHistory(historyJson)
        } catch (e: Exception) {
            android.util.Log.e("SearchHistoryManager", "保存历史记录失败", e)
        }
    }

    private fun baixing_fromJson(historyJson: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return try {
            mBaixing_gson.fromJson<List<String>>(historyJson, type)
        } catch (e: Exception) {
            android.util.Log.e("SearchHistoryManager", "解析历史记录失败", e)
            emptyList()
        }
    }
}