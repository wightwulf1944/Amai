package i.am.shiro.amai.model

data class DetailModel(
    val title: String,
    val pageCount: Int,
    val isFavorite: Boolean,
    val artistTags: List<TagModel>?,
    val groupTags: List<TagModel>?,
    val parodyTags: List<TagModel>?,
    val characterTags: List<TagModel>?,
    val languageTags: List<TagModel>?,
    val categoryTags: List<TagModel>?,
    val generalTags: List<TagModel>?,
    val thumbnails: List<Thumbnail>
)

data class TagModel(
    val type: String,
    val name: String
) {
    val query: String
        get() {
            val searchTag = if (name.any(Char::isWhitespace)) "\"$name\"" else name
            return "$type:$searchTag"
        }
}