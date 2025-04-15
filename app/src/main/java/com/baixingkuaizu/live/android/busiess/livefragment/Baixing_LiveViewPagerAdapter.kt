package com.baixingkuaizu.live.android.busiess.livefragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.baixingkuaizu.live.android.fragment.Baixing_LiveCategoryFragment
import com.baixingkuaizu.live.android.fragment.Baixing_LiveFragment

class Baixing_LiveViewPagerAdapter(
    private var mBaixing_categoryList: ArrayList<Baixing_CategoryDataEntity>,
    f: Baixing_LiveFragment
) : FragmentStateAdapter(f) {

    override fun getItemCount(): Int = mBaixing_categoryList.size

    override fun createFragment(position: Int): Fragment {
        val categoryId = mBaixing_categoryList[position].id
        return Baixing_LiveCategoryFragment.baixing_newInstance(categoryId)
    }
}