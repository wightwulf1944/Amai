package i.am.shiro.amai.data.view

import androidx.room.DatabaseView

@DatabaseView("""
    SELECT 
        bookId, 
        saveDate,
        title, 
        pageCount, 
        thumbnailWidth, 
        thumbnailHeight,
        thumbnailUrl
    FROM FavoriteEntity 
    LEFT JOIN BookEntity USING(bookId) 
""")
class SavedPreviewView(
    val bookId: Int,
    val saveDate: Long,
    val title: String,
    val pageCount: Int,
    val thumbnailWidth: Int,
    val thumbnailHeight: Int,
    val thumbnailUrl: String
)