package i.am.shiro.amai.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SavedEntity(
    @PrimaryKey
    val bookId: Int,
    val saveDate: Long
) {
    constructor(bookId: Int) : this (
        bookId = bookId,
        saveDate = System.currentTimeMillis()
    )
}