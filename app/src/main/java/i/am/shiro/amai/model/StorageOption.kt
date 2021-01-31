package i.am.shiro.amai.model

import android.os.Environment
import java.io.File

class StorageOption(dir: File) {

    val path = dir.path

    val isMounted = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState(dir)

    val spaceFree = dir.freeSpace

    val percentUsed = (dir.usedSpace / (dir.totalSpace / 100.0)).toInt()

}

private val File.usedSpace: Long
    get() = totalSpace - freeSpace