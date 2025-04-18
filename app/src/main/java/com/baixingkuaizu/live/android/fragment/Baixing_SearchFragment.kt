package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.searchfragment.Baixing_FlexboxHistoryAdapter
import com.baixingkuaizu.live.android.busiess.searchfragment.Baixing_SearchResultAdapter
import com.baixingkuaizu.live.android.viewmodel.Baixing_SearchViewModel
import com.baixingkuaizu.live.android.databinding.BaixingSearchFragmentBinding
import com.baixingkuaizu.live.android.os.Baixing_NetViewState
import com.baixingkuaizu.live.android.widget.toast.CenterToast

class Baixing_SearchFragment : Baixing_BaseFragment() {
    private lateinit var mBaixing_binding: BaixingSearchFragmentBinding

    private lateinit var mBaixing_viewModel: Baixing_SearchViewModel
    private var mBaixing_adapter: Baixing_SearchResultAdapter? = null
    private lateinit var mBaixing_historyAdapter: Baixing_FlexboxHistoryAdapter
    private var mBaixing_NetViewState: Baixing_NetViewState? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBaixing_binding = BaixingSearchFragmentBinding.inflate(inflater, container, false)
        return mBaixing_binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baixing_observeViewModel()
        baixing_initViews()
        mBaixing_viewModel.baixing_loadSearchHistory()
    }

    private fun baixing_initViews() {
        mBaixing_binding.run {
            mBaixing_NetViewState = Baixing_NetViewState(
                baixingRecyclerView,
                baixingEmptyView,
                baixingProgressBar,
            ).addListener(this@Baixing_SearchFragment)

            mBaixing_historyAdapter =
                Baixing_FlexboxHistoryAdapter(baixingRecyclerHistory) { keyword ->
                    baixingEditSearch.setText(keyword)
                    baixing_performSearch(keyword)
                }

            baixingEditSearch.apply {
                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        val keyword = text.toString().trim()
                        baixing_performSearch(keyword)
                        return@setOnEditorActionListener true
                    }
                    false
                }

                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        val isEmpty = s.isNullOrEmpty()
                        mBaixing_binding.baixingBtnClear.isVisible = !isEmpty

                        // 显示或隐藏搜索提示
                        if (!isEmpty) {
                            val keyword = s.toString().trim()
                            mBaixing_binding.baixingSearchSuggestion.text = "搜索\"${keyword}\""
                            mBaixing_binding.baixingSearchSuggestion.isVisible = true
                        } else {
                            mBaixing_binding.baixingSearchSuggestion.isVisible = false
                        }
                    }
                })
            }

            // 设置搜索提示点击事件
            baixingSearchSuggestion.setOnClickListener {
                val keyword = baixingEditSearch.text.toString().trim()
                baixing_performSearch(keyword)
            }

            // 设置搜索提示点击事件
            baixingSearchSuggestion.setOnClickListener {
                val keyword = baixingEditSearch.text.toString().trim()
                baixing_performSearch(keyword)
            }

            baixingBtnClear.setClick {
                baixingEditSearch.text.clear()
                baixingSearchSuggestion.isVisible = false
            }

            baixingBtnClearHistory.setClick {
                mBaixing_viewModel.baixing_clearSearchHistory()
            }

            baixingBtnBack.setClick {
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    private fun baixing_observeViewModel() {
        mBaixing_viewModel = ViewModelProvider(this)[Baixing_SearchViewModel::class.java].apply {
            mBaixing_searchResults.observe(viewLifecycleOwner) { results ->
                mBaixing_binding.baixingRecyclerView.apply {
                    isVisible = true
                    if (mBaixing_adapter == null) {
                        mBaixing_adapter = Baixing_SearchResultAdapter()
                        adapter = mBaixing_adapter
                        layoutManager = LinearLayoutManager(context)
                    }
                    mBaixing_adapter!!.submitList(results)
                }
                mBaixing_binding.baixingSearchSuggestion.isVisible = false
            }

            mBaixing_searchHistory.observe(viewLifecycleOwner) { history ->
                mBaixing_historyAdapter.submitList(history)
                // 当没有搜索历史数据时隐藏历史记录区域和清除按钮
                val hasHistory = history.isNotEmpty()
                mBaixing_binding.baixingHistoryLayout.isVisible = hasHistory
                mBaixing_binding.baixingBtnClearHistory.isVisible = hasHistory

                mBaixing_isLoading.observe(viewLifecycleOwner) {
                    mBaixing_binding.run {
                        if (it) {
                            mBaixing_NetViewState?.init()
                        }
                        baixingProgressBar.isVisible = it
                    }
                }

                mBaixing_toasterror.observe(viewLifecycleOwner) { error ->
                    if (!error.isNullOrEmpty()) {
                        CenterToast.show(activity, error)
                    }
                }

                mBaixing_empty.observe(viewLifecycleOwner) {
                    mBaixing_binding.baixingEmptyView.isVisible = it
                }
            }
        }
    }

    private fun baixing_performSearch(keyword: String) {
        if (keyword.isBlank()) {
            CenterToast.show(activity, "请输入搜索关键词")
            return
        }
        mBaixing_viewModel.baixing_search(keyword)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBaixing_adapter = null
    }
}