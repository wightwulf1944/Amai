package i.am.shiro.amai.model

data class BookPreview(
    val bookId: Int,
    val aspectRatio: Float,
    val thumbnailUrl: String,
    val title: String,
    val showFavoriteBadge: Boolean,
    val pageCount: Int
)