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
import i.am.shiro.amai.model.Tag
import kotlinx.serialization.Serializable

@Stable
class HomeScreenState(
    selectedTabState: MutableState<HomeScreenTab>,
    val nhentaiNavStack: NavBackStack<NavKey>
) {
    var selectedTab by selectedTabState

    fun goToLatest() {
        selectedTab = HomeScreenTab.NHENTAI
        nhentaiNavStack.clear()
        nhentaiNavStack.add(NhentaiRoute.Latest)
    }

    fun goToTag(tag: Tag) {
        selectedTab = HomeScreenTab.NHENTAI
        nhentaiNavStack.clear()
        nhentaiNavStack.add(NhentaiRoute.Tag(tag.id, tag.name))
    }

    fun goToSearch(query: String) {
        selectedTab = HomeScreenTab.NHENTAI
        nhentaiNavStack.clear()
        nhentaiNavStack.add(NhentaiRoute.Search(query))
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
    data class Tag(val tagId: Int, val tagName: String) : NhentaiRoute

    @Serializable
    data class Search(val query: String) : NhentaiRoute
}