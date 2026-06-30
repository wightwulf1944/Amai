package i.am.shiro.amai.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    primaryKeys = ["bookId", "type", "name"],
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["bookId"],
            childColumns = ["bookId"],
            onDelete = CASCADE
        )
    ]
)
class TagEntity(
    @ColumnInfo(defaultValue = "0")
    val id: Int,
    val bookId: Int,
    val type: String,
    val name: String
)
