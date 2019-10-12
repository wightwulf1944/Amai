package i.am.shiro.amai.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
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

    private static AmaiDatabase INSTANCE;

    public static AmaiDatabase getInstance() {
        return INSTANCE;
    }

    public static void init(Context context) {
        if (INSTANCE != null) throw new RuntimeException("Already initialized");

        INSTANCE = Room.databaseBuilder(context, AmaiDatabase.class, "books")
            .fallbackToDestructiveMigration()
            .build();
    }

    public abstract BookDao getBookDao();

    public abstract TagDao getTagDao();
}
