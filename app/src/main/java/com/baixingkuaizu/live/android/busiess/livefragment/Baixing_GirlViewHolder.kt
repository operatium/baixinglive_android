package com.baixingkuaizu.live.android.busiess.livefragment

import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.databinding.BaixingLiveItemBinding
import android.text.SpannableString
import android.text.Spanned
import com.bumptech.glide.Glide
import android.text.style.TextAppearanceSpan
import android.text.style.AbsoluteSizeSpan
import android.graphics.Typeface
import android.graphics.Color
import android.text.style.ForegroundColorSpan

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播列表项ViewHolder
 */
open class Baixing_GirlViewHolder(private val mBaixing_binding: BaixingLiveItemBinding)
    : RecyclerView.ViewHolder(mBaixing_binding.root) {
    
    fun baixing_clearImage() {
        Glide.with(mBaixing_binding.baixingLiveCover.context)
            .clear(mBaixing_binding.baixingLiveCover)
    }
    
    open fun baixing_bind(liveData: Baixing_GirlDataEntity) {
        mBaixing_binding.apply {
            val context = baixingLiveCover.context
            baixingLiveAnchorName.text = liveData.anchorName
            val count = liveData.viewerCount.let {
                if (it > 9999) "9999+" else it.toString()
            }
            
            val viewerText = SpannableString("${count}人观看")
            val numberEndIndex = count.length
            
            viewerText.setSpan(
                TextAppearanceSpan(baixingLiveViewerCount.context, android.R.style.TextAppearance).apply {
                    Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                },
                0,
                numberEndIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            viewerText.setSpan(
                AbsoluteSizeSpan(context.resources.getDimensionPixelSize(R.dimen.sp_12), false),
                0,
                numberEndIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            
            viewerText.setSpan(
                AbsoluteSizeSpan(context.resources.getDimensionPixelSize(R.dimen.sp_10), false),
                numberEndIndex,
                viewerText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            viewerText.setSpan(
                ForegroundColorSpan(Color.WHITE),
                0,
                viewerText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            
            baixingLiveViewerCount.text = viewerText
            
            Glide.with(baixingLiveCover.context)
                .load(liveData.coverUrl)
                .placeholder(R.drawable.baixing_def_cover)
                .into(baixingLiveCover)
        }
    }
}