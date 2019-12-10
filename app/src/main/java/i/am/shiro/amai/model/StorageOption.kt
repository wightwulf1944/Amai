package i.am.shiro.amai.model

import android.os.Environment
import java.io.File

class StorageOption(dir: File) {

    val path: String

    val isMounted: Boolean

    val spaceFree: Long

    val percentUsed: Int

    init {
        path = dir.path

        isMounted = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState(dir)

        spaceFree = dir.freeSpace

        percentUsed = (dir.usedSpace / (dir.totalSpace / 100.0)).toInt()
    }
}

private val File.usedSpace: Long
    get() = totalSpace - freeSpace