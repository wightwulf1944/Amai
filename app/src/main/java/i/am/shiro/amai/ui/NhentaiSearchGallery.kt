@file:SuppressLint("ModifierParameter")

package i.am.shiro.amai.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import i.am.shiro.amai.R
import i.am.shiro.amai.data.remote.Nhentai
import i.am.shiro.amai.model.BookPreview
import i.am.shiro.amai.ui.common.GalleryBody
import i.am.shiro.amai.ui.common.GallerySortMenu
import i.am.shiro.amai.ui.common.TopBarContainer
import i.am.shiro.amai.ui.common.TopBarPill
import i.am.shiro.amai.ui.viewmodel.NhentaiSearchViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NhentaiSearchGallery(
    searchQuery: String,
    sort: Nhentai.Sort,
    onSortChange: (Nhentai.Sort) -> Unit,
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    viewModel: NhentaiSearchViewModel = koinViewModel {
        parametersOf(searchQuery, sort)
    },
    sharedElementModifier: Modifier,
) {
    val books = viewModel.books.collectAsLazyPagingItems()

    SearchGalleryContent(
        query = searchQuery,
        sort = sort,
        books = books,
        onSortChange = onSortChange,
        onSearchClick = onSearchClick,
        onItemClick = onItemClick,
        sharedElementModifier = sharedElementModifier,
    )
}

@Composable
private fun SearchGalleryContent(
    query: String,
    sort: Nhentai.Sort,
    books: LazyPagingItems<BookPreview>,
    onSortChange: (Nhentai.Sort) -> Unit,
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    sharedElementModifier: Modifier,
) {
    Scaffold(
        topBar = {
            SearchGalleryTopBar(
                query = query,
                sort = sort,
                onSortChange = onSortChange,
                onSearchClick = onSearchClick,
                sharedElementModifier = sharedElementModifier,
            )
        },
        content = { innerPadding ->
            GalleryBody(
                books = books,
                onItemClick = onItemClick,
                contentPadding = innerPadding
            )
        }
    )
}

@Composable
private fun SearchGalleryTopBar(
    query: String,
    sort: Nhentai.Sort,
    onSortChange: (Nhentai.Sort) -> Unit,
    onSearchClick: () -> Unit,
    sharedElementModifier: Modifier,
) {
    TopBarContainer {
        TopBarPill(
            modifier = Modifier
                .weight(1f)
                .then(sharedElementModifier)
        ) {
            FlowRow(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(R.string.search),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.alignByBaseline()
                )
                Text(
                    text = query,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alignByBaseline()
                )
            }
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = stringResource(R.string.search)
                )
            }
        }
        TopBarPill {
            var expanded by remember { mutableStateOf(false) }
            IconButton(onClick = { expanded = true }) {
                Icon(
                    painter = painterResource(R.drawable.ic_sort),
                    contentDescription = stringResource(R.string.sort)
                )
            }
            GallerySortMenu(
                selected = sort,
                expanded = expanded,
                onDismissRequest = { expanded = false },
                onSortChange = onSortChange
            )
        }
    }
}
