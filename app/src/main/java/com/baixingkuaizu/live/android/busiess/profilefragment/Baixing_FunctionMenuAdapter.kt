package com.baixingkuaizu.live.android.busiess.profilefragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.os.Baixing_AdapterHelper.setClick
import com.baixingkuaizu.live.android.databinding.BaixingProfileMenuItemBinding

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 个人资料页面功能菜单适配器
 */
class Baixing_FunctionMenuAdapter(
    private val mBaixing_items: List<Baixing_FunctionMenuItem>,
    private val mBaixing_onItemClick: (Baixing_FunctionMenuItem) -> Unit
) : RecyclerView.Adapter<Baixing_FunctionMenuAdapter.Baixing_MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Baixing_MenuViewHolder {
        val binding = BaixingProfileMenuItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Baixing_MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Baixing_MenuViewHolder, position: Int) {
        val item = mBaixing_items[position]
        holder.baixing_bind(item)
    }

    override fun getItemCount(): Int = mBaixing_items.size

    inner class Baixing_MenuViewHolder(private val mBaixing_binding: BaixingProfileMenuItemBinding) :
        RecyclerView.ViewHolder(mBaixing_binding.root) {

        fun baixing_bind(item: Baixing_FunctionMenuItem) {
            mBaixing_binding.apply {
                baixingMenuIcon.setImageResource(item.mBaixing_iconResId)
                baixingMenuTitle.text = item.mBaixing_title
                
                // 设置点击事件
                root.setClick {
                    mBaixing_onItemClick(item)
                }
            }
        }
    }
}