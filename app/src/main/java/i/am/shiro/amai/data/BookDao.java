package i.am.shiro.amai.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface BookDao {

    // TODO make a preview version of this returning less columns
    @Query("SELECT * FROM BookEntity ORDER BY id DESC")
    LiveData<List<BookEntity>> findAllSorted();

    @Query("SELECT * FROM BookEntity WHERE id=:id")
    LiveData<BookEntity> findById(int id);

    @Insert(onConflict = REPLACE)
    void insert(List<BookEntity> bookEntities);

    @Query("DELETE FROM BookEntity")
    void deleteAll();
}
