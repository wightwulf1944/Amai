package i.am.shiro.amai.ui.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.TextRange
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import i.am.shiro.amai.data.repository.SearchRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(SavedStateHandleSaveableApi::class)
class SearchViewModel(
    handle: SavedStateHandle,
    initialQuery: String,
    private val repository: SearchRepository
) : ViewModel() {

    val textFieldState by handle.saveable(saver = TextFieldState.Saver) {
        TextFieldState(initialQuery)
    }

    val suggestionsFlow = snapshotFlow { textFieldState.text to textFieldState.selection }
        .map { (text, selection) ->
            if (selection.collapsed) {
                getSuggestions(text, selection)
            } else {
                emptyList() // Hide suggestions when text is selected
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // This naively looks for whitespace to differentiate tokens and does not recognize escaped whitespace
    fun getSuggestions(text: CharSequence, selection: TextRange): List<SearchSuggestion> {
        val cursorIndex = selection.start

        val start = text.lastIndexOf(' ', cursorIndex - 1)
            .let { if (it == -1) 0 else it + 1 }

        val end = text.indexOf(' ', cursorIndex)
            .let { if (it == -1) text.length else it }

        val currentWord = text.substring(start, cursorIndex)

        return repository.getQueryPrefixes(currentWord)
            .map {
                SearchSuggestion(it) {
                    textFieldState.edit {
                        replace(start, end, it)
                        this.selection = TextRange(start + it.length)
                    }
                }
            }
    }
}

data class SearchSuggestion(
    val text: String,
    val onClick: () -> Unit = {}
)
