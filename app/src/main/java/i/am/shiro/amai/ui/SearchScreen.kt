package i.am.shiro.amai.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.byValue
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import i.am.shiro.amai.R
import i.am.shiro.amai.ui.theme.AmaiTheme
import i.am.shiro.amai.ui.viewmodel.SearchSuggestion
import i.am.shiro.amai.ui.viewmodel.SearchViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    query: String,
    onSearch: (String) -> Unit,
    viewModel: SearchViewModel = koinViewModel()
) {
    SearchContent(
        initialQuery = query,
        onSearch = onSearch,
        suggestions = viewModel.suggestions,
        onQueryChange = viewModel::onQueryChange
    )
}

@Composable
fun SearchContent(
    initialQuery: String,
    onSearch: (String) -> Unit,
    suggestions: List<SearchSuggestion>,
    onQueryChange: (String) -> Unit
) {
    val textFieldState = rememberTextFieldState(initialQuery)

    LaunchedEffect(textFieldState.text) {
        onQueryChange(textFieldState.text.toString())
    }

    Scaffold { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {

            SearchInput(
                state = textFieldState,
                onSearch = { onSearch(textFieldState.text.toString()) }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(suggestions) { suggestion ->
                    SuggestionItem(
                        text = suggestion.text,
                        onClick = { textFieldState.setTextAndPlaceCursorAtEnd(suggestion.proposedValue) }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchInput(
    state: TextFieldState,
    onSearch: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TextField(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .focusRequester(focusRequester),
        placeholder = { Text(stringResource(R.string.search)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
            autoCorrectEnabled = false,
        ),
        onKeyboardAction = { if (state.text.isNotEmpty()) onSearch() },
        lineLimits = TextFieldLineLimits.SingleLine,
        inputTransformation = InputTransformation.byValue { _, proposed ->
            proposed.toString().lowercase()
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun SuggestionItem(
    text: String,
    onClick: () -> Unit
) {
    Column {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Preview
@Composable
private fun SearchContentPreview() {
    AmaiTheme {
        SearchContent(
            initialQuery = "",
            suggestions = listOf(SearchSuggestion("tag:artist"), SearchSuggestion("tag:artistic")),
            onQueryChange = {},
            onSearch = {},
        )
    }
}
