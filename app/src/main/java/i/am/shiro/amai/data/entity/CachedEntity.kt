package i.am.shiro.amai.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CachedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val bookId: Int
)