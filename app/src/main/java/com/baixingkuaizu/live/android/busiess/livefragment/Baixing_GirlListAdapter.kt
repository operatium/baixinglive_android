package com.baixingkuaizu.live.android.busiess.livefragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.baixingkuaizu.live.android.databinding.BaixingLiveItemBinding

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播列表适配器
 */
class Baixing_GirlListAdapter
    : ListAdapter<Baixing_GirlDataEntity, Baixing_GirlViewHolder>(Baixing_GirlDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Baixing_GirlViewHolder {
        return Baixing_GirlViewHolder(
            BaixingLiveItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Baixing_GirlViewHolder, position: Int) {
        holder.baixing_bind(getItem(position))
    }

    override fun onViewRecycled(holder: Baixing_GirlViewHolder) {
        super.onViewRecycled(holder)
        holder.baixing_clearImage()
    }
}