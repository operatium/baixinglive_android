package com.baixingkuaizu.live.android.busiess.livefragment

import java.util.concurrent.ConcurrentHashMap

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播数据缓存类，提供模拟数据
 */
object Baixing_LiveDataCache {
    private val mBaixing_data = ConcurrentHashMap<String, HashMap<Int, Baixing_LivePageEntity>>()
    private val mBaixing_page = ConcurrentHashMap<String, Int>()

    fun getListById(id:String): ArrayList<Baixing_LiveDataEntity> {
        val resultList = ArrayList<Baixing_LiveDataEntity>()
        val map = mBaixing_data[id] ?: return resultList
        val sortedList = map.entries.sortedWith(compareBy { it.key })
        for (item in sortedList) {
            resultList.addAll(item.value.data)
        }
        return resultList
    }

    fun addList(id:String, page:Baixing_LivePageEntity) {
        if (mBaixing_data[id] == null) {
            mBaixing_data[id] = HashMap()
        }
        mBaixing_data[id]!!.put(page.page, page)
    }

    fun getPage(id:String): Int {
        return mBaixing_page[id] ?: 0
    }

    fun incrementPage(id:String):Int {
        val page = mBaixing_page[id] ?: 0
        mBaixing_page[id] = page + 1
        return page + 1
    }

    fun clear(id:String) {
        mBaixing_page.remove(id)
        mBaixing_data.remove(id)
    }
}