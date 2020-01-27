package i.am.shiro.amai.data.view

import androidx.room.DatabaseView

@DatabaseView("""
    SELECT
        bookId,
        title,
        COUNT(*) AS progress,
        pageCount AS progressMax
    FROM DownloadJobEntity
    LEFT JOIN BookEntity USING(bookId)
    LEFT JOIN (SELECT * FROM LocalImageEntity) USING(bookId)
    GROUP BY bookId
    ORDER BY createTime
""")
class DownloadDetailView(
    val bookId: Int,
    val title: String,
    val progress: Int,
    val progressMax: Int
)
