package i.am.shiro.amai.model

import java.io.File

class StorageOption(dir: File) {

    val path: String = dir.path

    val spaceFree = dir.freeSpace

    val percentUsed = with(dir) { ((totalSpace - freeSpace) / (totalSpace / 100.0)).toInt() }

}
