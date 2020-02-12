package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import i.am.shiro.amai.data.entity.CachedEntity
import io.reactivex.Completable

@Dao
interface CachedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entities: List<CachedEntity>)

    @Query("DELETE FROM CachedEntity")
    fun deleteAll(): Completable
}