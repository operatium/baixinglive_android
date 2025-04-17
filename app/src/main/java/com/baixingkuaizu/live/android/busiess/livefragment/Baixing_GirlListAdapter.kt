package com.baixingkuaizu.live.android.busiess.livefragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.baixingkuaizu.live.android.databinding.BaixingLiveItemBinding

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播列表适配器
 */
class Baixing_GirlListAdapter : ListAdapter<Baixing_LiveDataEntity, Baixing_LiveViewHolder>(Baixing_LiveDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Baixing_LiveViewHolder {
        val binding = BaixingLiveItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        if (viewType == 1) {
            return Baixing_GirlFootViewHolder(binding)
        }
        return Baixing_LiveViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Baixing_LiveViewHolder, position: Int) {
        val item = getItem(position)
        holder.baixing_bind(item)
    }

    override fun onViewRecycled(holder: Baixing_LiveViewHolder) {
        super.onViewRecycled(holder)
        holder.baixing_clearImage()
    }

    override fun getItemViewType(position: Int): Int {
        if (currentList[position].id == "已经到底了")
            return 1
        return 0
    }
}