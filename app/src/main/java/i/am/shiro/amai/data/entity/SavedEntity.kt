package i.am.shiro.amai.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class SavedEntity(
    @PrimaryKey
    val bookId: Int,
    val saveDate: Long
) {
    @Ignore
    constructor(bookId: Int) : this (
        bookId = bookId,
        saveDate = System.currentTimeMillis()
    )
}