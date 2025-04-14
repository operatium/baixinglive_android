package com.baixingkuaizu.live.android.busiess.livefragment

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.databinding.BaixingLiveItemBinding
import android.text.SpannableString
import android.text.Spanned
import android.util.TypedValue
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
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
class Baixing_LiveViewHolder(private val mBaixing_binding: BaixingLiveItemBinding) : 
    RecyclerView.ViewHolder(mBaixing_binding.root) {
    
    fun baixing_clearImage() {
        // 清除图片加载请求
        Glide.with(mBaixing_binding.baixingLiveCover.context)
            .clear(mBaixing_binding.baixingLiveCover)
    }
    
    fun baixing_bind(liveData: Baixing_LiveData) {
        mBaixing_binding.apply {
            val context = baixingLiveCover.context
            // 设置主播名称
            baixingLiveAnchorName.text = liveData.anchorName
            
            // 设置观看人数
            val viewerText = SpannableString("${liveData.viewerCount}人观看")
            val numberEndIndex = liveData.viewerCount.toString().length
            
            // 设置数字部分为14sp加粗
            viewerText.setSpan(
                TextAppearanceSpan(baixingLiveViewerCount.context, android.R.style.TextAppearance).apply {
                    Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                },
                0,
                numberEndIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            viewerText.setSpan(
                AbsoluteSizeSpan(context.resources.getDimension(R.dimen.sp_16).toInt(), false),
                0,
                numberEndIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            
            // 设置'人观看'部分为12sp
            viewerText.setSpan(
                AbsoluteSizeSpan(context.resources.getDimension(R.dimen.sp_12).toInt(), false),
                numberEndIndex,
                viewerText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // 设置文本为白色
            viewerText.setSpan(
                ForegroundColorSpan(Color.WHITE),
                0,
                viewerText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            
            baixingLiveViewerCount.text = viewerText
            
            // 加载封面图片，使用Glide避免图片加载错位问题
            Glide.with(baixingLiveCover.context)
                .load(liveData.coverUrl)
                .placeholder(R.drawable.baixing_def_cover)
                .into(baixingLiveCover)
        }
    }
}