package i.am.shiro.amai.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
    foreignKeys = @ForeignKey(
        entity = BookEntity.class,
        parentColumns = "id",
        childColumns = "bookId",
        onDelete = CASCADE
    )
)
public class TagEntity {

    // this is different from the id received from server
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int bookId;

    private String type;

    private String name;

    public TagEntity(int id, int bookId, String type, String name) {
        this.id = id;
        this.bookId = bookId;
        this.type = type;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
