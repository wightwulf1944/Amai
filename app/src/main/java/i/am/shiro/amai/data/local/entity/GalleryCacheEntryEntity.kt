package i.am.shiro.amai.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.uuid.Uuid

@Entity(
    indices = [Index(value = ["cacheId", "bookId"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = GalleryCacheEntity::class,
            parentColumns = ["id"],
            childColumns = ["cacheId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GalleryCacheEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val cacheId: Uuid,
    val bookId: Int
)
