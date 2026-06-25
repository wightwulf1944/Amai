package i.am.shiro.amai.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    primaryKeys = ["bookId", "pageIndex"],
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["bookId"],
            childColumns = ["bookId"],
            onDelete = CASCADE
        )
    ]
)
data class ImageEntity(
    val bookId: Int,
    val pageIndex: Int,
    val width: Int,
    val height: Int,
    val path: String,
    val thumbnailWidth: Int,
    val thumbnailHeight: Int,
    val thumbnailPath: String
)