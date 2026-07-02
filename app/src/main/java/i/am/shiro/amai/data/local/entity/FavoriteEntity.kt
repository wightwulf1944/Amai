package i.am.shiro.amai.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.NO_ACTION

@Entity(
    primaryKeys = ["bookId"],
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["bookId"],
            childColumns = ["bookId"],
            onDelete = NO_ACTION
        )
    ]
)
class FavoriteEntity(
    val bookId: Int,
    val favoriteDate: Long = System.currentTimeMillis()
)
