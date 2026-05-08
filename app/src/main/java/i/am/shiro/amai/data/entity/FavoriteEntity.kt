package i.am.shiro.amai.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.NO_ACTION
import androidx.room.Ignore

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
data class FavoriteEntity(
    val bookId: Int,
    val saveDate: Long
) {
    @Ignore
    constructor(bookId: Int) : this (
        bookId = bookId,
        saveDate = System.currentTimeMillis()
    )
}