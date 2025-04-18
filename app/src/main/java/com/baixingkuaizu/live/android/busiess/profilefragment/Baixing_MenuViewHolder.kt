package com.baixingkuaizu.live.android.busiess.profilefragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.os.Baixing_AdapterHelper.setClick
import com.baixingkuaizu.live.android.databinding.BaixingProfileMenuItemBinding

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 个人资料页面功能菜单项ViewHolder
 */
class Baixing_MenuViewHolder(private val mBaixing_binding: BaixingProfileMenuItemBinding) :
    RecyclerView.ViewHolder(mBaixing_binding.root) {

    fun baixing_bind(item: Baixing_FunctionMenuItem, onItemClick: (Baixing_FunctionMenuItem) -> Unit) {
        mBaixing_binding.apply {
            baixingMenuIcon.setImageResource(item.mBaixing_iconResId)
            baixingMenuTitle.text = item.mBaixing_title
            
            root.setClick {
                onItemClick(item)
            }
        }
    }
    
    companion object {
        fun create(parent: ViewGroup): Baixing_MenuViewHolder {
            val binding = BaixingProfileMenuItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return Baixing_MenuViewHolder(binding)
        }
    }
}