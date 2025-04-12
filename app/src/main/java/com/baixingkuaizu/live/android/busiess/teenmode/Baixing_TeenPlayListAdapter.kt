package com.baixingkuaizu.live.android.busiess.teenmode

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.adatperandroid.Baixing_AdapterHelper.setClick
import com.bumptech.glide.Glide

/**
 * @author yuyuexing
 * @date: 2025/4/14
 * @description: 青少年模式播放列表适配器，使用DiffUtil优化更新性能
 */
class Baixing_TeenPlayListAdapter(
    private val mBaixing_onItemClick: (Baixing_VideoData) -> Unit
) : ListAdapter<Baixing_VideoData, Baixing_TeenPlayListAdapter.Baixing_ViewHolder>(DIFF_CALLBACK) {

    class Baixing_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBaixing_cover: ImageView = itemView.findViewById(R.id.baixing_video_cover)
        val mBaixing_title: TextView = itemView.findViewById(R.id.baixing_video_title)
        val mBaixing_author: TextView = itemView.findViewById(R.id.baixing_video_author)
        val mBaixing_duration: TextView = itemView.findViewById(R.id.baixing_video_duration)
        val mBaixing_tag: TextView = itemView.findViewById(R.id.baixing_video_tag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Baixing_ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.baixing_play_list_item, parent, false)
        return Baixing_ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Baixing_ViewHolder, position: Int) {
        val video = getItem(position)
        
        holder.mBaixing_title.text = video.mBaixing_title
        holder.mBaixing_author.text = video.mBaixing_author
        holder.mBaixing_duration.text = video.mBaixing_duration
        holder.mBaixing_tag.text = video.mBaixing_tag
        
        // 加载封面图片
        if (video.mBaixing_coverUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context).load(video.mBaixing_coverUrl).into(holder.mBaixing_cover)
        } else {
            // 如果没有封面图，加载默认封面
            Glide.with(holder.itemView.context).load(R.drawable.baixing_default_cover).into(holder.mBaixing_cover)
        }
        
        holder.itemView.setClick {
            mBaixing_onItemClick(video)
        }
    }

    fun baixing_filterByTag(tag: String) {
        if (tag.isEmpty() || tag == "全部") {
            submitList(mBaixing_allVideoList)
            return
        }
        
        val filteredList = mBaixing_allVideoList.filter { it.mBaixing_tag == tag }
        submitList(filteredList)
    }
    
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Baixing_VideoData>() {
            override fun areItemsTheSame(oldItem: Baixing_VideoData, newItem: Baixing_VideoData): Boolean {
                return oldItem.mBaixing_id == newItem.mBaixing_id
            }

            override fun areContentsTheSame(oldItem: Baixing_VideoData, newItem: Baixing_VideoData): Boolean {
                return oldItem == newItem
            }
        }
        
        // 所有视频的缓存列表
        val mBaixing_allVideoList = listOf(
            Baixing_VideoData(
                mBaixing_id = "1",
                mBaixing_title = "探索自然奥秘：动物世界大揭秘",
                mBaixing_author = "自然探索频道",
                mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
                mBaixing_duration = "05:30",
                mBaixing_tag = "自然"
            ),
            Baixing_VideoData(
                mBaixing_id = "2",
                mBaixing_title = "科学启蒙：奇妙化学实验",
                mBaixing_author = "科学课堂",
                mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
                mBaixing_duration = "08:15",
                mBaixing_tag = "科普"
            ),
            Baixing_VideoData(
                mBaixing_id = "3",
                mBaixing_title = "数学思维训练：几何图形的奥秘",
                mBaixing_author = "数学教育",
                mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
                mBaixing_duration = "10:22",
                mBaixing_tag = "教育"
            ),
            Baixing_VideoData(
                mBaixing_id = "4",
                mBaixing_title = "古代文明探索：埃及金字塔之谜",
                mBaixing_author = "历史探秘",
                mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
                mBaixing_duration = "12:45",
                mBaixing_tag = "历史"
            ),
            Baixing_VideoData(
                mBaixing_id = "5",
                mBaixing_title = "动画片：小熊历险记 第一集",
                mBaixing_author = "儿童动画工作室",
                mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
                mBaixing_duration = "15:00",
                mBaixing_tag = "动画"
            ),
            Baixing_VideoData(
                mBaixing_id = "6",
                mBaixing_title = "自然纪录片：海洋生物的奇妙世界",
                mBaixing_author = "自然探索频道",
                mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
                mBaixing_duration = "18:30",
                mBaixing_tag = "自然"
            ),
            Baixing_VideoData(
                mBaixing_id = "7",
                mBaixing_title = "物理知识：简单机械原理",
                mBaixing_author = "科学课堂",
                mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
                mBaixing_duration = "07:45",
                mBaixing_tag = "科普"
            ),
            Baixing_VideoData(
                mBaixing_id = "8",
                mBaixing_title = "语文名师讲解：古诗词鉴赏",
                mBaixing_author = "语文教育",
                mBaixing_coverUrl = "https://i.imgur.com/DvpvklR.png",
                mBaixing_duration = "09:50",
                mBaixing_tag = "教育"
            )
        )
    }
    
    init {
        submitList(mBaixing_allVideoList)
    }
} 