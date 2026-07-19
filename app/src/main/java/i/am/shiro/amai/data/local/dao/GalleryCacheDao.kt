package i.am.shiro.amai.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import i.am.shiro.amai.data.local.entity.GalleryCacheEntity
import kotlin.uuid.Uuid

@Dao
interface GalleryCacheDao {
    @Upsert
    suspend fun upsert(galleryCache: GalleryCacheEntity)

    @Query("SELECT * FROM GalleryCacheEntity WHERE id = :id")
    suspend fun cacheById(id: Uuid): GalleryCacheEntity?

    @Query("DELETE FROM GalleryCacheEntity WHERE id = :id")
    suspend fun deleteById(id: Uuid)

    @Query("DELETE FROM GalleryCacheEntity")
    suspend fun clearAll()
}
