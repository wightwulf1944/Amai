package i.am.shiro.amai.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["bookId"], unique = true)])
class CachedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val bookId: Int
)