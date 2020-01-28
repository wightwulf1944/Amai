package i.am.shiro.amai.data.view

import androidx.room.DatabaseView

@DatabaseView("""
    SELECT 
        bookId,
        title, 
        pageCount, 
        width AS thumbnailWidth, 
        height AS thumbnailHeight, 
        url AS thumbnailUrl,
        CASE 
            WHEN saveDate IS NULL THEN 0 ELSE 1 
        END AS isSaved
    FROM CachedEntity 
    LEFT JOIN BookEntity USING(bookId) 
    LEFT JOIN (SELECT * FROM ThumbnailView WHERE pageIndex = 0) USING(bookId)
    LEFT JOIN SavedEntity USING(bookId)
    ORDER BY id
""")
class CachedPreviewView(
    val bookId: Int,
    val title: String,
    val pageCount: Int,
    val thumbnailWidth: Int,
    val thumbnailHeight: Int,
    val thumbnailUrl: String,
    val isSaved: Boolean
)