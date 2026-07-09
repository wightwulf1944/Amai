package i.am.shiro.amai.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["cacheKey", "bookId"], unique = true)])
class CachedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(defaultValue = "")
    val cacheKey: String,
    val bookId: Int
)