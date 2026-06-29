package i.am.shiro.amai.data.repository

class SearchRepository {
    private val queryPrefixes = listOf(
        "id:",
        "tag:",
        "artist:",
        "parody:",
        "character:",
        "group:",
        "language:",
        "category:",
        "pages:",
        "pages:>",
        "pages:<",
        "favorites:",
        "favorites:>",
        "favorites:<",
        "uploaded:",
        "uploaded:>",
        "uploaded:<",
        "title:",
        "jtitle:"
    )

    fun getQueryPrefixes(query: String): List<String> {
        return queryPrefixes.filter { it.startsWith(query) }
    }
}
