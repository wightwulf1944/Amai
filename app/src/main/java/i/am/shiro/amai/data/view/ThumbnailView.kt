package i.am.shiro.amai.data.view

import androidx.room.DatabaseView

@DatabaseView(""" 
    SELECT 
        RemoteImageEntity.bookId,
        RemoteImageEntity.pageIndex,
        CASE
            WHEN LocalImageEntity.width IS NULL
            THEN RemoteImageEntity.width
            ELSE LocalImageEntity.width
        END AS width,
        CASE
            WHEN LocalImageEntity.height IS NULL
            THEN RemoteImageEntity.height
            ELSE LocalImageEntity.height
        END AS height,
        CASE 
            WHEN LocalImageEntity.thumbnailUrl IS NULL 
            THEN RemoteImageEntity.thumbnailUrl 
            ELSE LocalImageEntity.thumbnailUrl
        END AS url
    FROM RemoteImageEntity LEFT JOIN LocalImageEntity 
    ON RemoteImageEntity.bookId = LocalImageEntity.bookId
    AND RemoteImageEntity.pageIndex = LocalImageEntity.pageIndex
""")
class ThumbnailView(
    val bookId: Int,
    val pageIndex: Int,
    val width: Int,
    val height: Int,
    val url: String
)