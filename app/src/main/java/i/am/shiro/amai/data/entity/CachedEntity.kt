package i.am.shiro.amai.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CachedEntity(
    @PrimaryKey
    val bookId: Int,

    @Deprecated("")
    val cacheDate: Long = System.currentTimeMillis()
)