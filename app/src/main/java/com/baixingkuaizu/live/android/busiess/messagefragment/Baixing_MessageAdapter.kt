package com.baixingkuaizu.live.android.busiess.messagefragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.baixingkuaizu.live.android.databinding.BaixingMessageItemBinding

class Baixing_MessageAdapter : ListAdapter<Baixing_MessageItemEntity, Baixing_MessageViewHolder>(
    Baixing_MessageDiffCallback()
) {

    var baixing_onItemClick: ((Baixing_MessageItemEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Baixing_MessageViewHolder {
        val binding = BaixingMessageItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Baixing_MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Baixing_MessageViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            baixing_onItemClick?.invoke(item)
        }
    }
    
    override fun onViewRecycled(holder: Baixing_MessageViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }
}