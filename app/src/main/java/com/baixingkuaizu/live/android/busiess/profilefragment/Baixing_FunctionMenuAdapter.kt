package com.baixingkuaizu.live.android.busiess.profilefragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 个人资料页面功能菜单适配器
 */
class Baixing_FunctionMenuAdapter(
    private val mBaixing_items: List<Baixing_FunctionMenuItem>,
    private val mBaixing_onItemClick: (Baixing_FunctionMenuItem) -> Unit
) : RecyclerView.Adapter<Baixing_MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Baixing_MenuViewHolder {
        return Baixing_MenuViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: Baixing_MenuViewHolder, position: Int) {
        val item = mBaixing_items[position]
        holder.baixing_bind(item, mBaixing_onItemClick)
    }

    override fun getItemCount(): Int = mBaixing_items.size


}