package i.am.shiro.amai.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.byValue
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import i.am.shiro.amai.R
import i.am.shiro.amai.ui.common.TopBarContainer
import i.am.shiro.amai.ui.common.TopBarPill
import i.am.shiro.amai.ui.utils.SharedElementToken
import i.am.shiro.amai.ui.utils.sharedElement
import i.am.shiro.amai.ui.viewmodel.SearchSuggestion
import i.am.shiro.amai.ui.viewmodel.SearchViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

// TODO investigate why keyboard dismissal is delayed when leaving this screen
@Composable
fun SearchScreen(
    initialQuery: String,
    onSearch: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SearchViewModel = koinViewModel {
        parametersOf(initialQuery)
    },
    searchPillToken: SharedElementToken,
) {
    val suggestions by viewModel.suggestionsFlow.collectAsStateWithLifecycle()

    SearchContent(
        textFieldState = viewModel.textFieldState,
        onSearch = { onSearch(viewModel.textFieldState.text.toString()) },
        suggestions = suggestions,
        onBackClick = onBackClick,
        searchPillToken = searchPillToken,
    )
}

@Composable
fun SearchContent(
    textFieldState: TextFieldState,
    onBackClick: () -> Unit,
    onSearch: () -> Unit,
    suggestions: List<SearchSuggestion>,
    searchPillToken: SharedElementToken,
) {
    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.union(WindowInsets.ime),
        topBar = {
            SearchTopBar(
                textFieldState = textFieldState,
                onBackClick = onBackClick,
                onSearch = onSearch,
                searchPillToken = searchPillToken,
            )
        },
        content = { innerPadding ->
            SuggestionsColumn(
                innerPadding = innerPadding,
                suggestions = suggestions
            )
        }
    )
}

@Composable
fun SearchTopBar(
    onBackClick: () -> Unit,
    textFieldState: TextFieldState,
    onSearch: () -> Unit,
    searchPillToken: SharedElementToken,
) {
    TopBarContainer {
        TopBarPill {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.back)
                )
            }
        }
        val animatedVisibilityScope = LocalNavAnimatedContentScope.current
        TopBarPill(modifier = Modifier.sharedElement(searchPillToken, animatedVisibilityScope)) {
            SearchInput(
                textFieldState = textFieldState,
                onSearch = onSearch
            )
        }
    }
}

@Composable
fun SearchInput(
    textFieldState: TextFieldState,
    onSearch: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BasicTextField(
        state = textFieldState,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
            autoCorrectEnabled = false,
        ),
        onKeyboardAction = { onSearch() },
        lineLimits = TextFieldLineLimits.SingleLine,
        inputTransformation = InputTransformation.byValue { _, proposed ->
            proposed.toString().lowercase()
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = LocalContentColor.current
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        interactionSource = interactionSource,
        decorator = TextFieldDefaults.decorator(
            state = textFieldState,
            enabled = true,
            lineLimits = TextFieldLineLimits.SingleLine,
            outputTransformation = null,
            interactionSource = interactionSource,
            placeholder = { Text(stringResource(R.string.search)) },
            trailingIcon = {
                if (textFieldState.text.isNotEmpty()) {
                    IconButton(onClick = textFieldState::clearText) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = stringResource(R.string.clear)
                        )
                    }
                }
            },
            contentPadding = PaddingValues(start = 16.dp),
            container = {}
        )
    )
}

@Composable
private fun SuggestionsColumn(
    innerPadding: PaddingValues,
    suggestions: List<SearchSuggestion>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding + PaddingValues(bottom = 8.dp)
    ) {
        items(suggestions) { suggestion ->
            SuggestionItem(
                text = suggestion.text,
                onClick = suggestion.onClick
            )
            if (suggestion != suggestions.last()) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun SuggestionItem(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
