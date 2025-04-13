package com.baixingkuaizu.live.android.busiess.teenmode

import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.databinding.BaixingPlayListItemBinding

class Baixing_ViewHolder(val binding: BaixingPlayListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    val mBaixing_cover = binding.baixingVideoCover
    val mBaixing_title = binding.baixingVideoTitle
    val mBaixing_author = binding.baixingVideoAuthor
    val mBaixing_duration = binding.baixingVideoDuration
    val mBaixing_tag = binding.baixingVideoTag
}