package i.am.shiro.amai.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import i.am.shiro.amai.data.local.dao.BookDao
import i.am.shiro.amai.data.local.dao.CachedDao
import i.am.shiro.amai.data.local.dao.FavoriteDao
import i.am.shiro.amai.data.local.dao.ImageDao
import i.am.shiro.amai.data.local.dao.IntermediateDao
import i.am.shiro.amai.data.local.dao.TagDao
import i.am.shiro.amai.data.local.entity.BookEntity
import i.am.shiro.amai.data.local.entity.CachedEntity
import i.am.shiro.amai.data.local.entity.FavoriteEntity
import i.am.shiro.amai.data.local.entity.ImageEntity
import i.am.shiro.amai.data.local.entity.TagEntity

@Database(
    version = 32,
    exportSchema = true,
    entities = [
        BookEntity::class,
        TagEntity::class,
        ImageEntity::class,
        FavoriteEntity::class,
        CachedEntity::class
    ],
    autoMigrations = [
        AutoMigration(from = 27, to = 28, spec = AmaiDatabase.Migration27To28::class),
        AutoMigration(from = 28, to = 29, spec = AmaiDatabase.Migration28To29::class),
        AutoMigration(from = 29, to = 30),
        AutoMigration(from = 30, to = 31),
        AutoMigration(from = 31, to = 32),
    ]
)
@TypeConverters(Converters::class)
abstract class AmaiDatabase : RoomDatabase() {

    abstract val bookDao: BookDao

    abstract val tagDao: TagDao

    abstract val imageDao: ImageDao

    abstract val favoriteDao: FavoriteDao

    abstract val cachedDao: CachedDao

    abstract val intermediateDao: IntermediateDao

    @RenameColumn(
        tableName = "FavoriteEntity",
        fromColumnName = "saveDate",
        toColumnName = "favoriteDate"
    )
    class Migration27To28 : AutoMigrationSpec

    @RenameColumn(
        tableName = "BookEntity",
        fromColumnName = "thumbnailUrl",
        toColumnName = "thumbnailPath"
    )
    @RenameColumn(
        tableName = "ImageEntity",
        fromColumnName = "thumbnailUrl",
        toColumnName = "thumbnailPath"
    )
    @RenameColumn(
        tableName = "ImageEntity",
        fromColumnName = "url",
        toColumnName = "path"
    )
    class Migration28To29 : AutoMigrationSpec
}