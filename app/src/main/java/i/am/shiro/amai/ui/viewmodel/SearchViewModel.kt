package i.am.shiro.amai.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val dictionary = listOf(
        "id:",
        "tag:",
        "artist:",
        "parody:",
        "group:",
        "language:",
        "pages:",
        "pages:>",
        "pages:<",
        "uploaded:",
        "uploaded:>",
        "uploaded:<"
    )

    var suggestions by mutableStateOf(dictionary.map(::SearchSuggestion))
        private set

    // TODO: Use selection to provide suggestions based on cursor position instead of just splitting the string.
    fun onQueryChange(query: String) {
        val tokens = query.split(' ')
        val filtered = dictionary.filter { it.startsWith(tokens.last()) }
        val truncated = tokens.dropLast(1).joinToString(separator = " ")
        suggestions = if (truncated.isEmpty()) {
            filtered.map { SearchSuggestion(it) }
        } else {
            filtered.map { SearchSuggestion(it, "$truncated $it") }
        }
    }
}

data class SearchSuggestion(
    val text: String,
    val proposedValue: String = text
)