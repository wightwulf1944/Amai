package i.am.shiro.amai.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Home : Route

    @Serializable
    data object Search : Route

    @Serializable
    data class Detail(val bookId: Int) : Route

    @Serializable
    data class Read(val bookId: Int, val pageIndex: Int) : Route
}