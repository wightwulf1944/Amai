package i.am.shiro.amai.data.repository

import i.am.shiro.amai.data.remote.Nhentai
import i.am.shiro.amai.data.remote.dto.TagSearchRequestDto
import i.am.shiro.amai.data.remote.dto.TagSearchResponseDto
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

class SearchRepository(
    private val nhentaiApi: Nhentai.Api
) {
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

    private val validTagTypes = setOf(
        null,
        "artist",
        "category",
        "character",
        "group",
        "language",
        "parody",
        "tag"
    )

    suspend fun searchRemoteTags(operand: String): List<String> {
        val (type, query) = if (operand.contains(':')) {
            operand.substringBefore(':') to operand.substringAfter(':')
        } else {
            null to operand
        }

        // Don't search if prefix is not a valid server tag type
        if (type !in validTagTypes) return emptyList()

        val requestBody = TagSearchRequestDto(
            type = type,
            query = query.trim('"').ifEmpty { null },
        )

        return try {
            nhentaiApi.searchTag(requestBody).map(::formatTag)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.e(e)
            emptyList()
        }
    }

    private fun formatTag(tag: TagSearchResponseDto): String {
        val name = if (' ' in tag.name) "\"${tag.name}\"" else tag.name
        return "${tag.type}:$name"
    }
}
