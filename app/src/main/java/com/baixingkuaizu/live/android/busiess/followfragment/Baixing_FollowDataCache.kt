package com.baixingkuaizu.live.android.busiess.followfragment

/**
 * @author yuyuexing
 * @since 2025/4/18
 * @description: 关注主播数据缓存类
 */
object Baixing_FollowDataCache {
    private val mBaixing_followList = ArrayList<Baixing_FollowGirlEntity>()

    fun baixing_updateFollowList(list: List<Baixing_FollowGirlEntity>) {
        mBaixing_followList.clear()
        mBaixing_followList.addAll(list)
    }

    fun baixing_getOnlineList(): List<Baixing_FollowGirlEntity> {
        return mBaixing_followList
            .filter { it.isOnLine }
            .sortedByDescending { it.attention }
    }

    fun baixing_getOfflineList(): List<Baixing_FollowGirlEntity> {
        return mBaixing_followList
            .filter { !it.isOnLine }
            .sortedByDescending { it.attention }
    }

    fun baixing_clearCache() {
        mBaixing_followList.clear()
    }
}
