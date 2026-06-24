package i.am.shiro.amai.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import i.am.shiro.amai.R
import i.am.shiro.amai.compose.common.TopBarContainer
import i.am.shiro.amai.compose.common.TopBarPill
import i.am.shiro.amai.compose.utils.asSymmetricHorizontal
import i.am.shiro.amai.data.view.CachedPreviewView
import i.am.shiro.amai.viewmodel.HomepageViewModel
import org.koin.androidx.compose.koinViewModel

// TODO try jetpack paging library for loading content
@Composable
fun HomepageScreen(
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    viewModel: HomepageViewModel = koinViewModel()
) {
    val books by viewModel.books.collectAsStateWithLifecycle()

    val gridState = rememberLazyStaggeredGridState()

    LaunchedEffect(gridState.canScrollForward) {
        if (!gridState.canScrollForward) {
            viewModel.loadMore()
        }
    }

    HomepageContent(
        books = books,
        isLoading = viewModel.isLoading,
        onRefresh = viewModel::refresh,
        onSearchClick = onSearchClick,
        onItemClick = onItemClick,
        gridState = gridState
    )
}

@Composable
fun HomepageContent(
    books: List<CachedPreviewView>,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    gridState: LazyStaggeredGridState
) {
    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.asSymmetricHorizontal(0.5f),
        topBar = {
            HomepageTopBar(
                onSearchClick = onSearchClick
            )
        },
        content = { innerPadding ->
            BrowseBody(
                books = books,
                isLoading = isLoading,
                onRefresh = onRefresh,
                onItemClick = onItemClick,
                gridState = gridState,
                contentPadding = innerPadding
            )
        }
    )
}

@Composable
fun HomepageTopBar(
    onSearchClick: () -> Unit
) {
    TopBarContainer {
        TopBarPill {
            Text(
                text = stringResource(R.string.nhentai),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = stringResource(R.string.search)
                )
            }
        }
    }
}