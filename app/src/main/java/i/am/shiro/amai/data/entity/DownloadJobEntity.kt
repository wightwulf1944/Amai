package i.am.shiro.amai.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class DownloadJobEntity(
    @PrimaryKey
    val bookId: Int,
    val createTime: Long,
    val progressIndex: Int,
    val errorCount: Int,
    val isDownloading: Boolean,
    val isPaused: Boolean,
    val isDone: Boolean
) {
    constructor(bookId: Int) : this(
        bookId = bookId,
        createTime = System.currentTimeMillis(),
        progressIndex = 0,
        errorCount = 0,
        isDownloading = false,
        isPaused = false,
        isDone = false
    )
}