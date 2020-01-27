package i.am.shiro.amai

import i.am.shiro.amai.network.Nhentai

enum class NhentaiSort(private val stringValue: String) {

    New(Nhentai.SortOrder.DATE),
    Popular(Nhentai.SortOrder.POPULAR);

    override fun toString() = stringValue
}

enum class SavedSort {
    New, Old
}

enum class DownloadStatus {
    QUEUED, RUNNING, PAUSED, DONE, FAILED
}