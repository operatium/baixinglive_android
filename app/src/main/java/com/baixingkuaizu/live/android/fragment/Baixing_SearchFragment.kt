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
import com.baixingkuaizu.live.android.busiess.searchfragment.Baixing_SearchHistoryAdapter
import com.baixingkuaizu.live.android.busiess.searchfragment.Baixing_SearchResultAdapter
import com.baixingkuaizu.live.android.busiess.searchfragment.Baixing_SearchViewModel
import com.baixingkuaizu.live.android.databinding.BaixingSearchFragmentBinding
import com.baixingkuaizu.live.android.os.Baixing_NetViewState
import com.baixingkuaizu.live.android.widget.toast.CenterToast

class Baixing_SearchFragment : Baixing_BaseFragment() {
    private lateinit var mBaixing_binding: BaixingSearchFragmentBinding

    private lateinit var mBaixing_viewModel: Baixing_SearchViewModel
    private lateinit var mBaixing_adapter: Baixing_SearchResultAdapter
    private lateinit var mBaixing_historyAdapter: Baixing_SearchHistoryAdapter

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
    }

    private fun baixing_initViews() {
        mBaixing_binding.run {
            Baixing_NetViewState(
                baixingRecyclerView,
                baixingEmptyView,
                baixingProgressBar,
            ).addListener(this@Baixing_SearchFragment)
        }

        mBaixing_binding.baixingRecyclerView.apply {
            mBaixing_adapter = Baixing_SearchResultAdapter().also {
                adapter = it
            }
            layoutManager = LinearLayoutManager(context)
        }

        mBaixing_historyAdapter = Baixing_SearchHistoryAdapter { keyword ->
            mBaixing_binding.baixingEditSearch.setText(keyword)
            baixing_performSearch(keyword)
        }
        mBaixing_binding.baixingRecyclerHistory.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            adapter = mBaixing_historyAdapter
        }

        mBaixing_binding.baixingEditSearch.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val keyword = text.toString().trim()
                    baixing_performSearch(keyword)
                    return@setOnEditorActionListener true
                }
                false
            }

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    mBaixing_binding.baixingBtnClear.isVisible = !s.isNullOrEmpty()
                }
            })
        }

//            val keyword = mBaixing_binding.baixingEditSearch.text.toString().trim()
//            baixing_performSearch(keyword)

        mBaixing_binding.baixingBtnClear.setClick {
            mBaixing_binding.baixingEditSearch.text.clear()
        }

        mBaixing_binding.baixingBtnClearHistory.setClick {
            mBaixing_viewModel.baixing_clearSearchHistory()
        }

        mBaixing_binding.baixingBtnBack.setClick {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun baixing_observeViewModel() {
        mBaixing_viewModel = ViewModelProvider(this)[Baixing_SearchViewModel::class.java].apply {
            mBaixing_searchResults.observe(viewLifecycleOwner) { results ->
                mBaixing_adapter.submitList(results)
                mBaixing_binding.baixingEmptyView.isVisible = true
            }

            mBaixing_searchHistory.observe(viewLifecycleOwner) { history ->
                mBaixing_historyAdapter.submitList(history)
                mBaixing_binding.baixingHistoryLayout.isVisible = history.isNotEmpty()
            }

            mBaixing_isLoading.observe(viewLifecycleOwner) { isLoading ->
                mBaixing_binding.baixingProgressBar.isVisible = isLoading
                if (isLoading) {
                    mBaixing_binding.baixingEmptyView.isVisible = false
                } else {
                    mBaixing_binding.baixingEmptyView.isVisible = mBaixing_viewModel.mBaixing_searchResults.value?.isEmpty() == true
                }
            }

            mBaixing_error.observe(viewLifecycleOwner) { error ->
                if (!error.isNullOrEmpty()) {
                    CenterToast.show(activity, error)
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
    }
}