package i.am.shiro.amai.ui.navigation

import androidx.navigation3.runtime.NavKey
import i.am.shiro.amai.data.remote.Nhentai
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Home : Route

    @Serializable
    data class Search(val initialQuery: String, val initialSort: Nhentai.Sort) : Route

    @Serializable
    data class Detail(val bookId: Int) : Route

    @Serializable
    data class Read(val bookId: Int, val pageIndex: Int) : Route
}