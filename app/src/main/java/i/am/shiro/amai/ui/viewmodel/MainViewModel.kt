package i.am.shiro.amai.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import i.am.shiro.amai.ui.HomeTab
import kotlinx.serialization.Serializable

// TODO does this really need to be a viewmodel? Perhaps a HomeScreenState POJO
@OptIn(SavedStateHandleSaveableApi::class)
class MainViewModel(
    handle: SavedStateHandle
) : ViewModel() {

    var currentSearch by handle.saveable { mutableStateOf("") }

    var selectedHomeTab by handle.saveable { mutableStateOf(HomeTab.NHENTAI) }

    val nhentaiNavStack by handle.saved { NavBackStack<NhentaiRoute>(NhentaiRoute.Homepage) }

    fun search(query: String) {
        currentSearch = query
        selectedHomeTab = HomeTab.NHENTAI
        nhentaiNavStack.clear()
        nhentaiNavStack.add(NhentaiRoute.Browse(query))
    }

    fun search(tagId: Int) {
        TODO()
    }

    fun goToHomepage() {
        currentSearch = ""
        selectedHomeTab = HomeTab.NHENTAI
        nhentaiNavStack.clear()
        nhentaiNavStack.add(NhentaiRoute.Homepage)
    }
}

@Serializable
sealed interface NhentaiRoute : NavKey {
    @Serializable
    data class Browse(val query: String) : NhentaiRoute

    @Serializable
    object Homepage : NhentaiRoute
}