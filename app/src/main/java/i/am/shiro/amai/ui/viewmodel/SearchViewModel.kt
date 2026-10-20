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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.milliseconds

@OptIn(SavedStateHandleSaveableApi::class, FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel(
    handle: SavedStateHandle,
    private val repository: SearchRepository,
    initialQuery: String
) : ViewModel() {

    val textFieldState by handle.saveable(saver = TextFieldState.Saver) {
        TextFieldState(initialQuery)
    }

    // 1. Core input flow: emits the active term on every text/selection change
    private val activeTermFlow: Flow<ActiveTerm?> = snapshotFlow {
        findActiveTerm(textFieldState.text, textFieldState.selection)
    }

    // 2. Remote suggestions
    private val remoteTagsFlow: Flow<List<String>> = activeTermFlow
        .map { it?.term }
        .distinctUntilChanged()
        .debounce(300.milliseconds)
        .mapLatest { term ->
            if (term == null) emptyList()
            else repository.searchRemoteTags(term)
        }

    // 3. Combined UI suggestions flow
    val suggestionsFlow: StateFlow<List<SearchSuggestion>> = combine(
        activeTermFlow,
        remoteTagsFlow
    ) { activeTerm, remoteTags ->
        if (activeTerm == null) return@combine emptyList()

        val negationPrefix = if (activeTerm.isNegated) "-" else ""
        val cleanTerm = activeTerm.term.replace("\"", "")

        val matchingPrefixes = repository.getQueryPrefixes(activeTerm.term)
        val matchingRemoteTags = remoteTags.filter { it.replace("\"", "").contains(cleanTerm) }

        return@combine (matchingPrefixes + matchingRemoteTags).map { suggestionText ->
            SearchSuggestion(
                text = "$negationPrefix$suggestionText",
                onClick = {
                    val suffix = if (suggestionText.endsWith(':')) "" else " "
                    val textToInsert = "$negationPrefix$suggestionText$suffix"
                    textFieldState.edit {
                        replace(activeTerm.start, activeTerm.end, textToInsert)
                        selection = TextRange(activeTerm.start + textToInsert.length)
                    }
                }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}

private fun findActiveTerm(text: CharSequence, selection: TextRange): ActiveTerm? {
    if (!selection.collapsed) return null

    val cursor = selection.start

    var inQuotes = false
    var start = 0
    for (i in 0 until cursor) {
        when {
            text[i] == '"' -> inQuotes = !inQuotes
            text[i].isWhitespace() && !inQuotes -> start = i + 1
        }
    }

    var end = text.length
    var quoted = inQuotes
    for (i in cursor until text.length) {
        when {
            text[i] == '"' -> quoted = !quoted
            text[i].isWhitespace() && !quoted -> {
                end = i
                break
            }
        }
    }

    val rawTerm = text.substring(start, cursor)
    val isNegated = rawTerm.startsWith('-')
    val term = if (isNegated) rawTerm.substring(1) else rawTerm

    return ActiveTerm(start, end, isNegated, term)
}

class ActiveTerm(
    val start: Int,
    val end: Int,
    val isNegated: Boolean,
    val term: String,
)

data class SearchSuggestion(
    val text: String,
    val onClick: () -> Unit
)
