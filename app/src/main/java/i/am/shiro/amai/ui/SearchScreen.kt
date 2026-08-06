package i.am.shiro.amai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.byValue
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonShapes
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import i.am.shiro.amai.R
import i.am.shiro.amai.data.remote.Nhentai
import i.am.shiro.amai.ui.common.TopBarContainer
import i.am.shiro.amai.ui.common.TopBarPill
import i.am.shiro.amai.ui.theme.AmaiTheme
import i.am.shiro.amai.ui.utils.SharedElementToken
import i.am.shiro.amai.ui.utils.sharedElement
import i.am.shiro.amai.ui.viewmodel.SearchSuggestion
import i.am.shiro.amai.ui.viewmodel.SearchViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SearchScreen(
    initialQuery: String,
    initialSort: Nhentai.Sort,
    onSearch: (String, Nhentai.Sort) -> Unit,
    onDismissRequest: () -> Unit,
    viewModel: SearchViewModel = koinViewModel {
        parametersOf(initialQuery)
    },
    searchPillToken: SharedElementToken,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val suggestions by viewModel.suggestionsFlow.collectAsStateWithLifecycle()
    var selectedSort by rememberSaveable { mutableStateOf(initialSort) }

    SearchContent(
        textFieldState = viewModel.textFieldState,
        selectedSort = selectedSort,
        onSortChange = { selectedSort = it },
        onSearch = { onSearch(viewModel.textFieldState.text.toString(), selectedSort) },
        suggestions = suggestions,
        onDismissRequest = {
            keyboardController?.hide()
            onDismissRequest()
        },
        searchPillToken = searchPillToken,
    )
}

@Composable
fun SearchContent(
    textFieldState: TextFieldState,
    selectedSort: Nhentai.Sort,
    onSortChange: (Nhentai.Sort) -> Unit,
    onDismissRequest: () -> Unit,
    onSearch: () -> Unit,
    suggestions: List<SearchSuggestion>,
    searchPillToken: SharedElementToken,
) {
    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.union(WindowInsets.ime),
        topBar = {
            Column {
                SearchTopBar(
                    textFieldState = textFieldState,
                    onDismissRequest = onDismissRequest,
                    onSearch = onSearch,
                    searchPillToken = searchPillToken,
                )
                SortButtonGroup(
                    selectedSort = selectedSort,
                    onSortChange = onSortChange
                )
            }
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
    onDismissRequest: () -> Unit,
    textFieldState: TextFieldState,
    onSearch: () -> Unit,
    searchPillToken: SharedElementToken,
) {
    TopBarContainer {
        TopBarPill {
            IconButton(onClick = onDismissRequest) {
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
                onSearch = {
                    onDismissRequest()
                    onSearch()
                }
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
        textStyle = MaterialTheme.typography.bodyMedium.copy(
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
        contentPadding = innerPadding + PaddingValues(vertical = 8.dp)
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

@Composable
fun SortButtonGroup(
    selectedSort: Nhentai.Sort,
    onSortChange: (Nhentai.Sort) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .windowInsetsPadding(TopAppBarDefaults.windowInsets.only(WindowInsetsSides.Horizontal)),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
    ) {
        SortButton(
            text = stringResource(R.string.newest),
            selected = selectedSort == Nhentai.Sort.DATE,
            onSelect = { onSortChange(Nhentai.Sort.DATE) },
            shapes = ButtonGroupDefaults.connectedLeadingButtonShapes(),
        )
        SortButton(
            text = stringResource(R.string.popular),
            selected = selectedSort == Nhentai.Sort.POPULAR,
            onSelect = { onSortChange(Nhentai.Sort.POPULAR) },
            shapes = ButtonGroupDefaults.connectedMiddleButtonShapes(),
        )
        SortButton(
            text = stringResource(R.string.today),
            selected = selectedSort == Nhentai.Sort.POPULAR_TODAY,
            onSelect = { onSortChange(Nhentai.Sort.POPULAR_TODAY) },
            shapes = ButtonGroupDefaults.connectedMiddleButtonShapes(),
        )
        SortButton(
            text = stringResource(R.string.week),
            selected = selectedSort == Nhentai.Sort.POPULAR_WEEK,
            onSelect = { onSortChange(Nhentai.Sort.POPULAR_WEEK) },
            shapes = ButtonGroupDefaults.connectedMiddleButtonShapes(),
        )
        SortButton(
            text = stringResource(R.string.month),
            selected = selectedSort == Nhentai.Sort.POPULAR_MONTH,
            onSelect = { onSortChange(Nhentai.Sort.POPULAR_MONTH) },
            shapes = ButtonGroupDefaults.connectedTrailingButtonShapes()
        )
    }
}

@Composable
private fun RowScope.SortButton(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    shapes: ToggleButtonShapes
) {
    val size = ButtonDefaults.ExtraSmallContainerHeight
    ToggleButton(
        modifier = Modifier
            .weight(1f)
            .heightIn(size),
        checked = selected,
        onCheckedChange = { onSelect() },
        shapes = shapes,
        contentPadding = ButtonDefaults.contentPaddingFor(size),
        content = {
            Text(
                text = text,
                style = ButtonDefaults.textStyleFor(size)
            )
        }
    )
}

@Preview
@Composable
private fun PreviewSortButtonGroup() {
    AmaiTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            SortButtonGroup(
                selectedSort = Nhentai.Sort.DATE,
                onSortChange = {}
            )
        }
    }
}