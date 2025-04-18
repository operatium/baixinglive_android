package com.baixingkuaizu.live.android.busiess.searchfragment

import android.util.TypedValue
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.os.Baixing_AdapterHelper.getDimensionPixelSize
import com.google.android.flexbox.FlexboxLayout

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 搜索历史适配器 - FlexboxLayout版本
 */
class Baixing_FlexboxHistoryAdapter(private val parent: FlexboxLayout, private val onItemClick: (String) -> Unit) {
    private var mBaixing_data: List<String>? = null
    private val mBaixing_viewCache = mutableMapOf<String, TextView>()
    
    /**
     * 提交新的列表数据，使用DiffUtil计算差异，只更新变化的项
     */
    fun submitList(list: List<String>?) {
        list ?: return
        
        val oldList = mBaixing_data ?: emptyList()

        
        // 保存新列表
        mBaixing_data = list.toList()

        // 应用差异更新
        applyDiffResult(oldList, list)
    }
    
    /**
     * 应用DiffUtil计算的差异结果
     */
    private fun applyDiffResult(oldList: List<String>, newList: List<String>) {
        // 创建一个新的视图列表，初始包含所有旧视图
        val viewsToKeep = mutableSetOf<String>()
        
        // 移除不再需要的视图
        oldList.forEach { oldItem ->
            if (!newList.contains(oldItem)) {
                // 查找并移除此项的视图
                val viewToRemove = mBaixing_viewCache[oldItem]
                if (viewToRemove != null && viewToRemove.parent == parent) {
                    parent.removeView(viewToRemove)
                    mBaixing_viewCache.remove(oldItem)
                }
            } else {
                viewsToKeep.add(oldItem)
            }
        }
        
        // 添加新视图
        parent.removeAllViews() // 先清空所有视图，然后按新列表顺序重新添加
        
        newList.forEach { newItem ->
            val existingView = mBaixing_viewCache[newItem]
            if (existingView != null) {
                // 重用现有视图
                parent.addView(existingView)
            } else {
                // 创建新视图
                addTextView(newItem)
            }
        }
    }
    
    /**
     * 清空所有数据和视图
     */
    fun clear() {
        parent.removeAllViews()
        mBaixing_data = null
        mBaixing_viewCache.clear()
    }
    
    /**
     * 添加文本视图到FlexboxLayout
     */
    private fun addTextView(text: String) {
        val context = parent.context ?: return
        val tv = TextView(context).apply {
            setText(text)
            setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                context.getDimensionPixelSize(R.dimen.sp_12).toFloat()
            )
            setPadding(
                context.getDimensionPixelSize(R.dimen.dp_12),
                context.getDimensionPixelSize(R.dimen.dp_6),
                context.getDimensionPixelSize(R.dimen.dp_12),
                context.getDimensionPixelSize(R.dimen.dp_6),
            )
            setBackgroundResource(R.drawable.baixing_button_background3)
            setOnClickListener {
                onItemClick.invoke(text)
            }
        }
        
        // 设置布局参数，添加外边距
        val layoutParams = FlexboxLayout.LayoutParams(
            FlexboxLayout.LayoutParams.WRAP_CONTENT,
            FlexboxLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(
                0,
                0,
                context.getDimensionPixelSize(R.dimen.dp_8),
                context.getDimensionPixelSize(R.dimen.dp_8)
            )
        }
        tv.layoutParams = layoutParams
        
        // 缓存视图以便重用
        mBaixing_viewCache[text] = tv
        
        // 添加到布局
        parent.addView(tv)
    }
}