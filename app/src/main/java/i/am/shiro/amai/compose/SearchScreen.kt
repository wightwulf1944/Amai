package i.am.shiro.amai.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import i.am.shiro.amai.R
import i.am.shiro.amai.compose.common.AmaiTheme

// TODO: Use TextFieldValue.selection in SearchViewModel to provide suggestions based on cursor position instead of just splitting the string.
@Composable
fun SearchScreen(
    suggestions: List<String>,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearch: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    onQueryChange(textFieldValue)

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            TextField(
                value = textFieldValue,
                onValueChange = {
                    textFieldValue = it
                    onQueryChange(it)
                },
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
                keyboardActions = KeyboardActions(
                    onSearch = { onSearch(textFieldValue.text) }
                ),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(suggestions) { suggestion ->
                    SuggestionItem(
                        suggestion = suggestion,
                        onClick = {
                            val words = textFieldValue.text.split(' ')
                            val newText = words.dropLast(1).plus(suggestion).joinToString(" ")
                            textFieldValue = TextFieldValue(
                                text = newText,
                                selection = TextRange(newText.length)
                            )
                            onQueryChange(textFieldValue)
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun SuggestionItem(
    suggestion: String,
    onClick: () -> Unit
) {
    Text(
        text = suggestion,
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

@Preview
@Composable
private fun SearchScreenPreview() {
    AmaiTheme {
        SearchScreen(
            suggestions = listOf("tag:artist", "tag:artistic"),
            onQueryChange = {},
            onSearch = {},
        )
    }
}
