package i.am.shiro.amai.model

import android.os.Environment
import java.io.File

class StorageOption(dir: File) {

    val path: String = dir.path

    val isMounted = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState(dir)

    val spaceFree = dir.freeSpace

    val percentUsed = with(dir) { ((totalSpace - freeSpace) / (totalSpace / 100.0)).toInt() }

}
