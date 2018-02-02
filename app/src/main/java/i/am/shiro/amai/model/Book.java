package i.am.shiro.amai.model;

import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.squareup.moshi.FromJson;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static com.annimon.stream.Collectors.joining;
import static i.am.shiro.amai.retrofit.Nhentai.THUMBNAIL_BASE_URL;

/**
 * Created by Shiro on 1/6/2018.
 */

public class Book extends RealmObject {

    @PrimaryKey
    private int id;

    private String title;

    private int pageCount;

    private String thumbnailUrl;

    private int thumbnailWidth;

    private int thumbnailHeight;

    private String imageUrls;

    private String artistTags;

    private String languageTags;

    private String generalTags;

    public int getId() {
        return id;
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

    public static class MoshiAdapter {

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

        private static String getImageUrls(BookJson json) {
            return IntStream.range(1, json.pageCount)
                    .mapToObj(page -> THUMBNAIL_BASE_URL + page)
                    .collect(joining(","));
        }

        private static String getTagsByCategory(BookJson json, String category) {
            return Stream.of(json.tags)
                    .filter(tag -> tag.type.equals(category))
                    .map(tag -> tag.name)
                    .collect(joining(","));
        }

        @FromJson
        Book from(BookJson json) {
            Book book = new Book();
            book.id = json.id;
            book.title = json.title.english;
            book.pageCount = json.pageCount;
            book.thumbnailUrl = thumbnailUrlFrom(json);
            book.thumbnailWidth = json.images.thumbnail.width;
            book.thumbnailHeight = json.images.thumbnail.height;
            book.imageUrls = getImageUrls(json);
            book.artistTags = getTagsByCategory(json, "artist");
            book.languageTags = getTagsByCategory(json, "language");
            book.generalTags = getTagsByCategory(json, "tag");
            return book;
        }
    }
}
