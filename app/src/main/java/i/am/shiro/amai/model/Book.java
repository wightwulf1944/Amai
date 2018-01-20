package i.am.shiro.amai.model;

import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;

import java.util.List;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;

import static com.annimon.stream.Collectors.toList;

/**
 * Created by Shiro on 1/6/2018.
 */

public class Book implements RealmModel {

    private static final String THUMBNAIL_BASE_URL = "https://t.nhentai.net/galleries/";

    private static final String GALLERY_BASE_URL = "https://i.nhentai.net/galleries/";

    @PrimaryKey
    private int id;

    private String title;

    private int pageCount;

    private String thumbnailUrl;

    private int thumbnailWidth;

    private int thumbnailHeight;

    private List<String> imageUrls;

    private List<String> artistTags;

    private List<String> languageTags;

    private List<String> generalTags;

    public Book(BookJson bookJson) {
        id = bookJson.id;

        title = bookJson.title.english;

        pageCount = bookJson.pageCount;

        thumbnailUrl = thumbnailUrlFrom(bookJson);
        thumbnailWidth = bookJson.images.thumbnail.width;
        thumbnailHeight = bookJson.images.thumbnail.height;

        imageUrls = IntStream.range(1, bookJson.pageCount)
                .mapToObj(json -> THUMBNAIL_BASE_URL + json)
                .collect(toList());

        artistTags = Stream.of(bookJson.tags)
                .filter(json -> json.type.equals("artist"))
                .map(json -> json.name)
                .collect(toList());

        languageTags = Stream.of(bookJson.tags)
                .filter(json -> json.type.equals("language"))
                .map(json -> json.name)
                .collect(toList());

        generalTags = Stream.of(bookJson.tags)
                .filter(json -> json.type.equals("tag"))
                .map(json -> json.name)
                .collect(toList());
    }

    private static String thumbnailUrlFrom(BookJson bookJson) {
        return THUMBNAIL_BASE_URL + bookJson.mediaId + "/thumb" + fileExtensionFrom(bookJson.images.thumbnail);
    }

    private static String fileExtensionFrom(BookJson.Image image) {
        switch (image.type) {
            case "j":
                return ".jpg";
            case "p":
                return ".png";
            default:
                throw new RuntimeException("Unknown type " + image.type);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getPageCountStr() {
        return String.valueOf(pageCount);
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public int getThumbnailHeight() {
        return thumbnailHeight;
    }
}
