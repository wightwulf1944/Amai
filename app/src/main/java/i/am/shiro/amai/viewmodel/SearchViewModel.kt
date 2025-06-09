package i.am.shiro.amai.viewmodel

import android.text.Editable
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

    fun onTextInput(s: Editable) {
        val lastToken = s.split(' ').last()
        suggestionsLive.value = if (lastToken.isEmpty()) {
            dictionary
        } else {
            dictionary.filter { it.startsWith(lastToken, ignoreCase = true) }
        }
    }
}
