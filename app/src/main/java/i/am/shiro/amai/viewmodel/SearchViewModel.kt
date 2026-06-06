package i.am.shiro.amai.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
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

    val suggestionsLive = MutableLiveData(dictionary)

    // TODO: Use value.selection to provide suggestions based on cursor position instead of just splitting the string.
    fun onQueryChange(value: TextFieldValue) {
        val s = value.text
        val lastToken = s.split(' ').last()
        suggestionsLive.value = if (lastToken.isEmpty()) {
            dictionary
        } else {
            dictionary.filter { it.startsWith(lastToken, ignoreCase = true) }
        }
    }
}
