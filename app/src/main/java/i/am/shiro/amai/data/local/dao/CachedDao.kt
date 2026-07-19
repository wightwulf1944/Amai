package i.am.shiro.amai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import i.am.shiro.amai.data.local.entity.CachedEntity
import kotlin.uuid.Uuid

@Dao
interface CachedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CachedEntity)

    @Query("DELETE FROM CachedEntity WHERE cacheKey = :cacheKey")
    suspend fun clearCache(cacheKey: Uuid)

    @Query("DELETE FROM CachedEntity")
    suspend fun clearCache()
}
