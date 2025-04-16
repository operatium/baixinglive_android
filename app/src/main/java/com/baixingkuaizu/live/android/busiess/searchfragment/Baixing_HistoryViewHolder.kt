package com.baixingkuaizu.live.android.busiess.searchfragment

import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.databinding.BaixingSearchHistoryItemBinding

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 搜索历史视图持有者，负责绑定搜索历史数据到视图
 */
class Baixing_HistoryViewHolder(private val binding: BaixingSearchHistoryItemBinding, 
                               private val onItemClick: (String) -> Unit) : 
        RecyclerView.ViewHolder(binding.root) {
    
    fun bind(keyword: String) {
        binding.baixingTvKeyword.text = keyword
        binding.root.setOnClickListener { onItemClick(keyword) }
    }
    fun unbind() {
        binding.root.setOnClickListener(null)
    }
}