package i.am.shiro.amai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import i.am.shiro.amai.data.local.entity.GalleryCacheEntryEntity

@Dao
interface GalleryCacheEntryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entry: GalleryCacheEntryEntity)
}
