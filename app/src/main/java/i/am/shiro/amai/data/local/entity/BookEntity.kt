package i.am.shiro.amai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class BookEntity(
    @PrimaryKey
    val bookId: Int,
    val title: String,
    val pageCount: Int,
    val thumbnailWidth: Int,
    val thumbnailHeight: Int,
    val thumbnailPath: String
)
