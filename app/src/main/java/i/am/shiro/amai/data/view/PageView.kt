package i.am.shiro.amai.data.view

import androidx.room.DatabaseView

@DatabaseView("""
    SELECT 
        RemoteImageEntity.bookId, 
        RemoteImageEntity.pageIndex,
        CASE
            WHEN LocalImageEntity.url IS NULL 
            THEN RemoteImageEntity.url
            ELSE LocalImageEntity.url
        END AS url,
        CASE 
            WHEN LocalImageEntity.thumbnailUrl IS NULL 
            THEN RemoteImageEntity.thumbnailUrl 
            ELSE LocalImageEntity.thumbnailUrl
        END AS thumbnailUrl
    FROM RemoteImageEntity LEFT JOIN LocalImageEntity
    ON RemoteImageEntity.bookId = LocalImageEntity.bookId
    AND RemoteImageEntity.pageIndex = LocalImageEntity.pageIndex
""")
class PageView(
    val bookId: Int,
    val pageIndex: Int,
    val url: String,
    val thumbnailUrl: String
)