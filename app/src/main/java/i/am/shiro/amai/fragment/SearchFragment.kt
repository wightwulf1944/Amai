package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.SuggestionsAdapter
import i.am.shiro.amai.databinding.FragmentSearchBinding
import i.am.shiro.amai.viewmodel.MainViewModel
import i.am.shiro.amai.viewmodel.SearchViewModel

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel by viewModels<SearchViewModel>()

    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentSearchBinding.bind(view)

        val adapter = SuggestionsAdapter()

        viewModel.suggestionsLive.observe(viewLifecycleOwner) {
            adapter.suggestions = it
        }

        b.suggestionsRecycler.adapter = adapter

        adapter.onSuggestionCLickListener = {
            val before = b.textInput.text
            val after = before.split(' ')
                .dropLast(1)
                .plus(it)
                .joinToString(" ")
            b.textInput.setText(after)
            b.textInput.setSelection(after.length)
        }

        b.textInput.doAfterTextChanged {
            viewModel.onTextInput(it!!)
        }
        b.textInput.onImeActionSearch {
            val search = b.textInput.text.toString()
            b.textInput.setText("")
            activityViewModel.searchEventLive.value = MainViewModel.SearchEvent(search)
        }
    }
}

private fun TextView.onImeActionSearch(listener: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            listener()
            return@setOnEditorActionListener true
        }
        return@setOnEditorActionListener false
    }
}