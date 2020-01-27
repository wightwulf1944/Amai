package i.am.shiro.amai.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["bookId", "pageIndex"],
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["bookId"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class RemoteImageEntity(
    val bookId: Int,
    val pageIndex: Int,
    val width: Int,
    val height: Int,
    val url: String,
    val thumbnailWidth: Int,
    val thumbnailHeight: Int,
    val thumbnailUrl: String
)