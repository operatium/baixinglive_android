package com.baixingkuaizu.live.android.busiess.searchfragment

import androidx.recyclerview.widget.DiffUtil

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 搜索历史差异对比回调，用于优化RecyclerView的更新
 */
class Baixing_SearchHistoryDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}