package i.am.shiro.amai.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import i.am.shiro.amai.data.remote.Nhentai
import i.am.shiro.amai.model.Tag
import kotlinx.serialization.Serializable

@Stable
class HomeScreenState(
    selectedTabState: MutableState<HomeScreenTab>,
    val nhentaiNavStack: NavBackStack<NavKey>
) {
    var selectedTab by selectedTabState

    fun goToLatest() = goTo(NhentaiRoute.Latest)

    fun goToTag(tag: Tag) = goTo(NhentaiRoute.Tag(tag.id, tag.name, tag.query))

    fun goToSearch(query: String) = goTo(NhentaiRoute.Search(query))

    private fun goTo(destination: NavKey) {
        selectedTab = HomeScreenTab.NHENTAI
        if (nhentaiNavStack.lastOrNull() != destination) {
            nhentaiNavStack.add(destination)
        }
    }
}

@Composable
@Suppress("UNCHECKED_CAST")
fun rememberHomeScreenState(): HomeScreenState {
    val selectedTabState = rememberSaveable { mutableStateOf(HomeScreenTab.NHENTAI) }
    val mutableNavStack = rememberNavBackStack(NhentaiRoute.Latest)
    return remember {
        HomeScreenState(
            selectedTabState = selectedTabState,
            nhentaiNavStack = mutableNavStack
        )
    }
}

enum class HomeScreenTab {
    FAVORITES, NHENTAI
}

@Serializable
sealed interface NhentaiRoute : NavKey {
    @Serializable
    data object Latest : NhentaiRoute

    @Serializable
    data class Tag(
        val id: Int,
        val name: String,
        val query: String,
        val sort: Nhentai.Sort = Nhentai.Sort.DATE
    ) : NhentaiRoute

    @Serializable
    data class Search(
        val query: String,
        val sort: Nhentai.Sort = Nhentai.Sort.DATE
    ) : NhentaiRoute
}