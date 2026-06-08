package i.am.shiro.amai.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    private val _suggestions = MutableStateFlow(dictionary)
    val suggestions = _suggestions.asStateFlow()

    // TODO: Use value.selection to provide suggestions based on cursor position instead of just splitting the string.
    fun onQueryChange(value: TextFieldValue) {
        val s = value.text
        val lastToken = s.split(' ').last()
        _suggestions.value = if (lastToken.isEmpty()) {
            dictionary
        } else {
            dictionary.filter { it.startsWith(lastToken, ignoreCase = true) }
        }
    }
}
