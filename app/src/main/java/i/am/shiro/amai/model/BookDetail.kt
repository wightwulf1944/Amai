package i.am.shiro.amai.model

data class BookDetail(
    val title: String,
    val pageCount: Int,
    val isFavorite: Boolean,
    val artistTags: List<Tag>?,
    val groupTags: List<Tag>?,
    val parodyTags: List<Tag>?,
    val characterTags: List<Tag>?,
    val languageTags: List<Tag>?,
    val categoryTags: List<Tag>?,
    val generalTags: List<Tag>?,
    val thumbnails: List<Thumbnail>
)

// TODO type is not used anywhere but here. Construct query elswehere and remove type
data class Tag(
    val type: String,
    val name: String
) {
    val query: String
        get() {
            val searchTag = if (name.any(Char::isWhitespace)) "\"$name\"" else name
            return "$type:$searchTag"
        }
}