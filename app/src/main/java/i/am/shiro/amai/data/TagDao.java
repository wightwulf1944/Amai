package i.am.shiro.amai.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TagDao {

    @Insert(onConflict = REPLACE)
    void insert(List<TagEntity> tagEntities);

    @Query("SELECT * FROM TagEntity WHERE bookId=:bookId AND type=:type")
    LiveData<List<TagEntity>> findByFilter(int bookId, String type);
}
