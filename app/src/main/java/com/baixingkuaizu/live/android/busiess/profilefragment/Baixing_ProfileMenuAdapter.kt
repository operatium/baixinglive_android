package com.baixingkuaizu.live.android.busiess.profilefragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.os.Baixing_AdapterHelper.setClick
import com.baixingkuaizu.live.android.databinding.BaixingProfileMenuItemBinding

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 个人资料页面菜单适配器
 */
class Baixing_ProfileMenuAdapter(
    private val baixing_menuItems: List<Baixing_MenuItem>,
    private val baixing_onItemClick: (Baixing_MenuItem) -> Unit
) : RecyclerView.Adapter<Baixing_ProfileMenuAdapter.Baixing_MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Baixing_MenuViewHolder {
        val binding = BaixingProfileMenuItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Baixing_MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Baixing_MenuViewHolder, position: Int) {
        holder.bind(baixing_menuItems[position])
    }

    override fun getItemCount(): Int = baixing_menuItems.size

    inner class Baixing_MenuViewHolder(private val binding: BaixingProfileMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menuItem: Baixing_MenuItem) {
            binding.baixingMenuIcon.setImageResource(menuItem.baixing_iconResId)
            binding.baixingMenuTitle.text = menuItem.baixing_title
            
            binding.root.setClick {
                baixing_onItemClick(menuItem)
            }
        }
    }
}