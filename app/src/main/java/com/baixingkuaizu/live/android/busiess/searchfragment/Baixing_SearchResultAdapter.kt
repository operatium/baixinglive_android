package com.baixingkuaizu.live.android.busiess.searchfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.baixingkuaizu.live.android.databinding.BaixingSearchResultItemBinding

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 搜索结果适配器，负责管理搜索结果列表的显示
 */
class Baixing_SearchResultAdapter: ListAdapter<Baixing_SearchAnchorEntity, Baixing_SearchViewHolder>(Baixing_SearchResultDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Baixing_SearchViewHolder {
        val binding = BaixingSearchResultItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Baixing_SearchViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: Baixing_SearchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onViewRecycled(holder: Baixing_SearchViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }
}