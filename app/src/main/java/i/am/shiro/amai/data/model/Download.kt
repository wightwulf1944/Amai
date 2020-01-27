package i.am.shiro.amai.data.model

import i.am.shiro.amai.DownloadStatus

class Download(
    val bookId: Int,
    val title: String,
    val progress: Int,
    val progressMax: Int,
    val status: DownloadStatus
)