package i.am.shiro.amai.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
    entities = {
        BookEntity.class,
        TagEntity.class
    },
    version = 4,
    exportSchema = false
)
public abstract class AmaiDatabase extends RoomDatabase {

    public abstract BookDao getBookDao();

    public abstract TagDao getTagDao();
}
