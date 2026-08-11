package i.am.shiro.amai.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import i.am.shiro.amai.data.local.dao.BookDao
import i.am.shiro.amai.data.local.dao.FavoriteDao
import i.am.shiro.amai.data.local.dao.GalleryCacheDao
import i.am.shiro.amai.data.local.dao.GalleryCacheEntryDao
import i.am.shiro.amai.data.local.dao.ImageDao
import i.am.shiro.amai.data.local.dao.IntermediateDao
import i.am.shiro.amai.data.local.dao.TagDao
import i.am.shiro.amai.data.local.entity.BookEntity
import i.am.shiro.amai.data.local.entity.FavoriteEntity
import i.am.shiro.amai.data.local.entity.GalleryCacheEntity
import i.am.shiro.amai.data.local.entity.GalleryCacheEntryEntity
import i.am.shiro.amai.data.local.entity.ImageEntity
import i.am.shiro.amai.data.local.entity.TagEntity

@Database(
    version = 34,
    exportSchema = true,
    entities = [
        BookEntity::class,
        TagEntity::class,
        ImageEntity::class,
        FavoriteEntity::class,
        GalleryCacheEntity::class,
        GalleryCacheEntryEntity::class
    ],
    autoMigrations = [
        AutoMigration(from = 27, to = 28, spec = AmaiDatabase.Migration27To28::class),
        AutoMigration(from = 28, to = 29, spec = AmaiDatabase.Migration28To29::class),
        AutoMigration(from = 29, to = 30),
        AutoMigration(from = 30, to = 31),
        AutoMigration(from = 31, to = 32),
        AutoMigration(from = 33, to = 34),
    ]
)
@TypeConverters(Converters::class)
abstract class AmaiDatabase : RoomDatabase() {

    abstract val bookDao: BookDao

    abstract val tagDao: TagDao

    abstract val imageDao: ImageDao

    abstract val favoriteDao: FavoriteDao

    abstract val galleryCacheDao: GalleryCacheDao

    abstract val galleryCacheEntryDao: GalleryCacheEntryDao

    abstract val intermediateDao: IntermediateDao

    companion object {
        val MIGRATION_32_33 = object : Migration(32, 33) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Drop old cache tables
                db.execSQL("DROP TABLE IF EXISTS CachedEntity")
                db.execSQL("DROP TABLE IF EXISTS RemoteKeyEntity")

                // Create new cache tables
                db.execSQL("CREATE TABLE IF NOT EXISTS GalleryCacheEntity (id TEXT NOT NULL, nextPage INTEGER, PRIMARY KEY(id))")
                db.execSQL("CREATE TABLE IF NOT EXISTS GalleryCacheEntryEntity (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, cacheId TEXT NOT NULL, bookId INTEGER NOT NULL, FOREIGN KEY(cacheId) REFERENCES GalleryCacheEntity(id) ON UPDATE NO ACTION ON DELETE CASCADE )")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_GalleryCacheEntryEntity_cacheId_bookId ON GalleryCacheEntryEntity (cacheId, bookId)")
            }
        }
    }

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