package com.baixingkuaizu.live.android.busiess.searchfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.baixingkuaizu.live.android.databinding.BaixingSearchHistoryItemBinding

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 搜索历史适配器，负责管理搜索历史列表的显示
 */
class Baixing_SearchHistoryAdapter(private val onItemClick: (String) -> Unit) : 
        ListAdapter<String, Baixing_HistoryViewHolder>(Baixing_SearchHistoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Baixing_HistoryViewHolder {
        val binding = BaixingSearchHistoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Baixing_HistoryViewHolder(binding, onItemClick)
    }
    override fun onBindViewHolder(holder: Baixing_HistoryViewHolder, position: Int) {
        val keyword = getItem(position)
        holder.bind(keyword)
    }
    override fun onViewRecycled(holder: Baixing_HistoryViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }
}