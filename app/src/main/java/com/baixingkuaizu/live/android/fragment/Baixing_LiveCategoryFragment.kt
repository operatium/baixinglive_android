package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveListAdapter
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveListCache
import com.baixingkuaizu.live.android.databinding.BaixingLiveCategoryFragmentBinding
import com.baixingkuaizu.live.android.widget.toast.CenterToast

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播栏目子页面Fragment，支持上拉加载和下拉刷新
 */
class Baixing_LiveCategoryFragment : Baixing_BaseFragment() {
    
    private var mBaixing_binding: BaixingLiveCategoryFragmentBinding? = null
    private val mBaixing_liveAdapter = Baixing_LiveListAdapter()
    private var mBaixing_categoryId: String = ""
    private var mBaixing_isLoading = false
    
    companion object {
        private const val ARG_CATEGORY_ID = "category_id"
        
        fun newInstance(categoryId: String): Baixing_LiveCategoryFragment {
            return Baixing_LiveCategoryFragment().apply {
                arguments = bundleOf(ARG_CATEGORY_ID to categoryId)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaixing_categoryId = arguments?.getString(ARG_CATEGORY_ID) ?: ""
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_binding = BaixingLiveCategoryFragmentBinding.inflate(inflater, container, false)
        return mBaixing_binding?.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baixing_initView()
        baixing_loadData()
    }
    
    private fun baixing_initView() {
        mBaixing_binding?.apply {
            // 设置RecyclerView
            val layoutManager = GridLayoutManager(context, 2)
            baixingLiveRecyclerView.layoutManager = layoutManager
            baixingLiveRecyclerView.adapter = mBaixing_liveAdapter
            
            // 设置下拉刷新
            baixingLiveSwipeRefresh.setOnRefreshListener {
                baixing_refreshData()
            }
            
            // 设置上拉加载更多
            baixingLiveRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    
                    if (dy > 0) { // 向下滚动
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                        
                        if (!mBaixing_isLoading && 
                            (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5 && 
                            firstVisibleItemPosition >= 0) {
                            baixing_loadMoreData()
                        }
                    }
                }
            })
        }
    }
    
    private fun baixing_loadData() {
        val liveDataList = Baixing_LiveListCache.getInstance().baixing_getLiveDataList(mBaixing_categoryId)
        mBaixing_liveAdapter.submitList(liveDataList.toList())
        baixing_updateFooterVisibility()
    }
    
    private fun baixing_refreshData() {
        Baixing_LiveListCache.getInstance().baixing_refreshData(mBaixing_categoryId)
        baixing_loadData()
        mBaixing_binding?.baixingLiveSwipeRefresh?.isRefreshing = false
        CenterToast.show(activity, "刷新成功")
    }
    
    private fun baixing_loadMoreData() {
        if (mBaixing_isLoading) return
        
        mBaixing_isLoading = true
        mBaixing_binding?.baixingLiveFooterLoading?.visibility = View.VISIBLE
        
        // 模拟网络延迟
        mBaixing_binding?.root?.postDelayed({
            val hasMore = Baixing_LiveListCache.getInstance().baixing_loadMoreData(mBaixing_categoryId)
            baixing_loadData()
            mBaixing_isLoading = false
            mBaixing_binding?.baixingLiveFooterLoading?.visibility = View.GONE
            
            if (!hasMore) {
                baixing_updateFooterVisibility()
            }
        }, 800)
    }
    
    private fun baixing_updateFooterVisibility() {
        val hasMore = Baixing_LiveListCache.getInstance().baixing_hasMoreData(mBaixing_categoryId)
        mBaixing_binding?.baixingLiveFooterEnd?.visibility = if (!hasMore) View.VISIBLE else View.GONE
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        mBaixing_binding = null
    }
}