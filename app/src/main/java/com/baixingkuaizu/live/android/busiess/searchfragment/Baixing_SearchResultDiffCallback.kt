package com.baixingkuaizu.live.android.busiess.searchfragment

import androidx.recyclerview.widget.DiffUtil

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 搜索结果差异对比回调，用于优化RecyclerView的更新
 */
class Baixing_SearchResultDiffCallback : DiffUtil.ItemCallback<Baixing_SearchAnchorEntity>() {
    override fun areItemsTheSame(oldItem: Baixing_SearchAnchorEntity, newItem: Baixing_SearchAnchorEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Baixing_SearchAnchorEntity, newItem: Baixing_SearchAnchorEntity): Boolean {
        return oldItem == newItem
    }
}