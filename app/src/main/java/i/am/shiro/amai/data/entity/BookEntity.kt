package i.am.shiro.amai.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookEntity(
    @PrimaryKey
    val bookId: Int,
    val webUrl: String,
    val title: String,
    val pageCount: Int,
    val uploadDate: Long,
    val favCount: Int
)