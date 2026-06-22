package i.am.shiro.amai.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
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

    // TODO: Use value.selection to provide suggestions based on cursor position instead of just splitting the string.
    fun onQueryChange(value: TextFieldValue) {
        val tokens = value.text.split(' ')

        val lastToken = tokens.last()
        val filtered = if (lastToken.isEmpty()) {
            dictionary
        } else {
            dictionary.filter { it.startsWith(lastToken, ignoreCase = true) }
        }

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
) {
    val textFieldValue
        get() = TextFieldValue(
            text = proposedValue,
            selection = TextRange(proposedValue.length)
        )
}