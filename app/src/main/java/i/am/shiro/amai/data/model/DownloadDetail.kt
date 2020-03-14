package i.am.shiro.amai.data.model

class DownloadDetail(
    val bookId: Int,
    val title: String,
    val pageCount: Int,
    val errorCount: Int,
    val isPaused: Boolean,
    val localImageCount: Int
)