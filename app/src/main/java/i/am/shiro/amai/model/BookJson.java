package i.am.shiro.amai.model;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by Shiro on 1/10/2018.
 */

public class BookJson {
    @Json(name = "id") int id;
    @Json(name = "media_id") String mediaId;
    @Json(name = "upload_date") long uploadDate;
    @Json(name = "num_favorites") int favCount;
    @Json(name = "num_pages") int pageCount;
    @Json(name = "scanlator") String scanlator;
    @Json(name = "title") Title title;
    @Json(name = "tags") List<Tag> tags;
    @Json(name = "images") ImageBundle images;

    public static class Title {
        @Json(name = "japanese") String japanese;
        @Json(name = "pretty") String pretty;
        @Json(name = "english") String english;
    }

    public static class ImageBundle {
        @Json(name = "cover") Image cover;
        @Json(name = "pages") List<Image> pages;
        @Json(name = "thumbnail") Image thumbnail;
    }

    public static class Image {
        @Json(name = "h") int height;
        @Json(name = "w") int width;
        @Json(name = "t") String type;
    }

    public static class Tag {
        @Json(name = "id") int id;
        @Json(name = "name") String name;
        @Json(name = "type") String type;
        @Json(name = "url") String url;
        @Json(name = "count") int count;
    }
}
