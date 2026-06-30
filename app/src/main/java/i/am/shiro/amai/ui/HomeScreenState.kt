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
import kotlinx.serialization.Serializable

@Stable
class HomeScreenState(
    selectedTabState: MutableState<HomeScreenTab>,
    val nhentaiNavStack: NavBackStack<NavKey>
) {
    var selectedTab by selectedTabState

    fun search(query: String) {
        selectedTab = HomeScreenTab.NHENTAI
        nhentaiNavStack.clear()
        nhentaiNavStack.add(NhentaiRoute.Browse(query))
    }

    fun search(tagId: Int) {
        TODO()
    }

    fun goToHomepage() {
        selectedTab = HomeScreenTab.NHENTAI
        nhentaiNavStack.clear()
        nhentaiNavStack.add(NhentaiRoute.Homepage)
    }
}

@Composable
@Suppress("UNCHECKED_CAST")
fun rememberHomeScreenState(): HomeScreenState {
    val selectedTabState = rememberSaveable { mutableStateOf(HomeScreenTab.NHENTAI) }
    val mutableNavStack = rememberNavBackStack(NhentaiRoute.Homepage)
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
    data class Browse(val query: String) : NhentaiRoute

    @Serializable
    data object Homepage : NhentaiRoute
}