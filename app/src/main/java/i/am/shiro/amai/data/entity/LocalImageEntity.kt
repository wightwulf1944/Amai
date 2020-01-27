package i.am.shiro.amai.data.entity

import androidx.room.Entity

@Entity(
    primaryKeys = ["bookId", "pageIndex"]
)
class LocalImageEntity(
    val bookId: Int,
    val pageIndex: Int,
    val width: Int,
    val height: Int,
    val url: String,
    val thumbnailWidth: Int,
    val thumbnailHeight: Int,
    val thumbnailUrl: String
)