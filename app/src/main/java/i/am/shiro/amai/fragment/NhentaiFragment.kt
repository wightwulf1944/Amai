package i.am.shiro.amai.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.text.set
import androidx.core.text.toSpannable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.CachedPreviewAdapter
import i.am.shiro.amai.databinding.FragmentNhentaiBinding
import i.am.shiro.amai.fragment.dialog.NhentaiSortDialog
import i.am.shiro.amai.util.amaiStatefulViewModels
import i.am.shiro.amai.util.dpToPx
import i.am.shiro.amai.util.goToDetail
import i.am.shiro.amai.util.show
import i.am.shiro.amai.viewmodel.Home
import i.am.shiro.amai.viewmodel.MainViewModel
import i.am.shiro.amai.viewmodel.NavigationPath
import i.am.shiro.amai.viewmodel.Nhentai
import i.am.shiro.amai.viewmodel.NhentaiViewModel
import i.am.shiro.amai.viewmodel.Search

// TODO try Jetpack Paging 3 library for infinite scrolling
class NhentaiFragment : Fragment(R.layout.fragment_nhentai) {

    private val viewModel by amaiStatefulViewModels<NhentaiViewModel>()

    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentNhentaiBinding.bind(view)

        b.sortButton.setOnClickListener {
            childFragmentManager.show(NhentaiSortDialog())
        }

        b.searchButton.setOnClickListener {
            activityViewModel.navigationPathLive.value = NavigationPath("", Home, Search)
        }

        b.swipeRefreshLayout.setProgressViewOffset(false, 0, 64.dpToPx())
        b.swipeRefreshLayout.setOnRefreshListener {
            b.swipeRefreshLayout.isRefreshing = false
            viewModel.onRefresh()
        }

        val adapter = CachedPreviewAdapter(
            onItemClick = { goToDetail(it.bookId) },
            onPositionBind = viewModel::onPositionBind
        )

        b.recyclerView.setHasFixedSize(true)
        b.recyclerView.adapter = adapter

        viewModel.booksLive.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.isLoadingLive.observe(viewLifecycleOwner) { isLoading ->
            b.progressBar.isVisible = isLoading
        }

        activityViewModel.navigationPathLive.observe(viewLifecycleOwner) { path ->
            path.traverse(Nhentai) {
                val search = path.payload
                b.titleView.text = search.tokenize()
                b.recyclerView.scrollToPosition(0)
                viewModel.onSearch(search)
            }
        }
    }
}

private fun String.tokenize(): Spannable {
    var searchMode = SearchMode.START
    var startI = -1
    val spannable = toSpannable()

    forEachIndexed { i, c ->
        when (searchMode) {
            SearchMode.START -> {
                when (c) {
                    '"' -> {
                        startI = i
                        searchMode = SearchMode.END_QUOTE
                    }

                    ':' -> {
                        // unexpected, do nothing
                    }

                    ' ' -> {
                        // stay in START
                    }

                    else -> {
                        startI = i
                        searchMode = SearchMode.END
                    }
                }
            }

            SearchMode.CONTINUE -> {
                when (c) {
                    '"' -> {
                        searchMode = SearchMode.END_QUOTE
                    }

                    ':' -> {
                        // unexpected, do nothing
                    }

                    ' ' -> {
                        // unexpected, end current token
                        spannable[startI .. i] = UnderlineSpan()
                        searchMode = SearchMode.START
                    }

                    else -> {
                        searchMode = SearchMode.END
                    }
                }
            }

            SearchMode.END -> {
                when (c) {
                    '"' -> {
                        // unexpected, end current token and start new token
                        spannable[startI .. i] = UnderlineSpan()
                        startI = i
                        searchMode = SearchMode.END_QUOTE
                    }

                    ':' -> {
                        searchMode = SearchMode.CONTINUE
                    }

                    ' ' -> {
                        spannable[startI .. i] = UnderlineSpan()
                        searchMode = SearchMode.START
                    }

                    else -> {
                        // stay in END
                    }
                }
            }

            SearchMode.END_QUOTE -> {
                when (c) {
                    '"' -> {
                        spannable[startI .. i + 1] = UnderlineSpan()
                        searchMode = SearchMode.START
                    }

                    else -> {
                        // inside quotes: do nothing
                    }
                }
            }
        }
    }
    if (searchMode != SearchMode.START) {
        spannable[startI .. length] = UnderlineSpan()
    }

    return spannable
}

private enum class SearchMode {
    START, CONTINUE, END, END_QUOTE
}