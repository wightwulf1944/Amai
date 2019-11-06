package i.am.shiro.amai.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        BookEntity::class,
        TagEntity::class
    ],
    version = 4,
    exportSchema = false)
abstract class AmaiDatabase : RoomDatabase() {

    abstract val bookDao: BookDao

    abstract val tagDao: TagDao
}
