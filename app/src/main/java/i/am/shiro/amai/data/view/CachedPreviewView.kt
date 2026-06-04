package i.am.shiro.amai.data.view

import androidx.room.DatabaseView

@DatabaseView("""
    SELECT 
        bookId,
        title, 
        pageCount, 
        thumbnailWidth, 
        thumbnailHeight, 
        thumbnailUrl,
        CASE 
            WHEN favoriteDate IS NULL THEN 0 ELSE 1 
        END AS isFavorite
    FROM CachedEntity 
    LEFT JOIN BookEntity USING(bookId) 
    LEFT JOIN FavoriteEntity USING(bookId)
    ORDER BY id
""")
data class CachedPreviewView(
    val bookId: Int,
    val title: String,
    val pageCount: Int,
    val thumbnailWidth: Int,
    val thumbnailHeight: Int,
    val thumbnailUrl: String,
    val isFavorite: Boolean
)