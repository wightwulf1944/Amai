package i.am.shiro.amai.model

import java.io.Serializable

data class SearchEvent(
    val query: String,
    var isProcessing: Boolean = true
) : Serializable
