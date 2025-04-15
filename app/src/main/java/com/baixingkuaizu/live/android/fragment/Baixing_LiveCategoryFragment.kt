package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveListAdapter
import com.baixingkuaizu.live.android.databinding.BaixingLiveCategoryFragmentBinding
import com.baixingkuaizu.live.android.viewmodel.Baixing_LiveCategoryViewModel
import com.baixingkuaizu.live.android.widget.toast.CenterToast

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播栏目子页面Fragment，支持上拉加载和下拉刷新
 */
class Baixing_LiveCategoryFragment : Baixing_BaseFragment() {
    
    private lateinit var mBaixing_binding: BaixingLiveCategoryFragmentBinding
    private var mBaixing_liveAdapter: Baixing_LiveListAdapter = Baixing_LiveListAdapter()
    private var mBaixing_categoryId: String = ""
    private var mBaixing_isLoading = false
    private var mBaixing_isBottom = false
    private lateinit var mBaixing_viewModel: Baixing_LiveCategoryViewModel
    
    companion object {
        val TAG = "yyx类Baixing_LiveCategoryFragment"
        private const val ARG_CATEGORY_ID = "category_id"

        fun baixing_newInstance(categoryId: String): Baixing_LiveCategoryFragment {
            return Baixing_LiveCategoryFragment().apply {
                arguments = bundleOf(ARG_CATEGORY_ID to categoryId)
            }
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_categoryId = arguments?.getString(ARG_CATEGORY_ID) ?: ""
        Log.d(TAG, "onCreateView: $mBaixing_categoryId")
        mBaixing_binding = BaixingLiveCategoryFragmentBinding.inflate(inflater, container, false)
        return mBaixing_binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBaixing_viewModel = ViewModelProvider(this)[Baixing_LiveCategoryViewModel::class.java].run {
            mBaixing_binding.loading.visibility = View.VISIBLE
            requestFirstPage(mBaixing_categoryId)
            liveList.observe(viewLifecycleOwner) {
                Log.d(TAG, "$mBaixing_categoryId liveList: ${it.count()} viewModel:${mBaixing_viewModel}")
                if (it.isEmpty()) {
                    baixing_empty()
                } else {
                    baixing_initListView()
                    mBaixing_liveAdapter.run {
                        submitList(it)
                        Log.d(TAG, "$mBaixing_categoryId submitList: ${it.count()}")
                    }

                    // 显示内容视图
                    mBaixing_binding.baixingLiveContentLayout.visibility = View.VISIBLE
                    mBaixing_binding.baixingLiveErrorLayout.visibility = View.GONE
                    mBaixing_binding.baixingLiveEmptyLayout.visibility = View.GONE
                }
            }
            isListBottom.observe(viewLifecycleOwner) {
                mBaixing_isBottom = it
                mBaixing_binding.baixingLiveFooterEnd.visibility =
                    if (it) View.VISIBLE else View.GONE
            }
            netError.observe(viewLifecycleOwner) {
                baixing_retry()
            }
            listloading.observe(viewLifecycleOwner) {
                if (!it) {
                    mBaixing_binding.baixingLiveSwipeRefresh.isRefreshing = false
                    mBaixing_binding.baixingLiveFooterLoading.visibility = View.GONE
                    mBaixing_binding.loading.visibility = View.GONE
                }
            }
            this
        }
    }
    
    private fun baixing_initListView() {
        mBaixing_binding.apply {
            // 设置RecyclerView
            val layoutManager = GridLayoutManager(context, 2)
            baixingLiveRecyclerView.layoutManager = layoutManager
                baixingLiveRecyclerView.adapter = mBaixing_liveAdapter

            
            // 设置下拉刷新
            baixingLiveSwipeRefresh.setOnRefreshListener {
                if (mBaixing_isLoading) return@setOnRefreshListener
                mBaixing_isLoading = true
                mBaixing_viewModel.requestFirstPage(mBaixing_categoryId)
            }
            
            // 设置上拉加载更多
            baixingLiveRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) { // 向下滚动
                        if (mBaixing_isLoading || mBaixing_isBottom) return
                        mBaixing_isLoading = true
                        mBaixing_binding.baixingLiveFooterLoading.visibility = View.VISIBLE
                        mBaixing_viewModel.requestNextPage(mBaixing_categoryId)
                    }
                }
            })
        }
    }
    
    /**
     * 显示网络错误界面并设置重试按钮点击事件
     */
    private fun baixing_retry() {
        mBaixing_binding.apply {
            // 隐藏内容和空视图
            baixingLiveContentLayout.visibility = View.GONE
            baixingLiveEmptyLayout.visibility = View.GONE
            
            // 显示错误视图
            baixingLiveErrorLayout.visibility = View.VISIBLE
            
            // 设置重试按钮点击事件
            baixingLiveErrorRetryBtn.setOnClickListener {
                CenterToast.show(activity, "正在重新加载...")

                // 重新请求数据
                mBaixing_isLoading = true
                mBaixing_viewModel.requestFirstPage(mBaixing_categoryId)
            }
        }
    }
    
    /**
     * 显示数据为空的界面
     */
    private fun baixing_empty() {
        mBaixing_binding.apply {
            // 隐藏内容和错误视图
            baixingLiveContentLayout.visibility = View.GONE
            baixingLiveErrorLayout.visibility = View.GONE
            
            // 显示空视图
            baixingLiveEmptyLayout.visibility = View.VISIBLE
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: $mBaixing_categoryId")
    }
}