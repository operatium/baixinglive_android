package com.baixingkuaizu.live.android.busiess.messagefragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.databinding.BaixingMessageItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Baixing_MessageViewHolder(private val binding: BaixingMessageItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private var glideRequestManager: RequestManager? = null

    fun bind(item: Baixing_MessageItemEntity) {
        glideRequestManager = Glide.with(binding.root.context).also {
            it.load(item.baixing_avatarUrl)
            .placeholder(R.drawable.baixing_def_cover)
            .into(binding.baixingIvAvatar)
        }

        binding.baixingTvTitle.text = item.baixing_title
        binding.baixingTvContent.text = item.baixing_content
        binding.baixingTvTime.text = formatTime(item.baixing_timestamp)

        if (item.baixing_unreadCount > 0) {
            binding.baixingTvUnread.visibility = View.VISIBLE
            binding.baixingTvUnread.text = if (item.baixing_unreadCount > 99) "99+" else item.baixing_unreadCount.toString()
        } else {
            binding.baixingTvUnread.visibility = View.GONE
        }

        binding.baixingTvBadge.visibility = when {
            item.baixing_isOfficial -> {
                binding.baixingTvBadge.text = "官方"
                View.VISIBLE
            }
            item.baixing_isMemberService -> {
                binding.baixingTvBadge.text = "会员服务"
                View.VISIBLE
            }
            else -> View.GONE
        }
    }

    private fun formatTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 60 * 1000 -> "刚刚"
            diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}分钟前"
            diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}小时前"
            else -> {
                val format = SimpleDateFormat("MM-dd", Locale.getDefault())
                format.format(Date(timestamp))
            }
        }
    }
    
    fun unbind() {
        try {
            glideRequestManager?.clear(binding.baixingIvAvatar)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        glideRequestManager = null
    }
}