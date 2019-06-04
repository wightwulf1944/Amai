package i.am.shiro.amai.model;

import com.squareup.moshi.Json;

import java.util.List;

public class BookJson {
    @Json(name = "id") public int id;
    @Json(name = "media_id") public String mediaId;
    @Json(name = "upload_date") public long uploadDate;
    @Json(name = "num_favorites") public int favCount;
    @Json(name = "num_pages") public int pageCount;
    @Json(name = "scanlator") public String scanlator;
    @Json(name = "title") public Title title;
    @Json(name = "tags") public List<Tag> tags;
    @Json(name = "images") public ImageBundle images;

    public static class Title {
        @Json(name = "japanese") public String japanese;
        @Json(name = "pretty") public String pretty;
        @Json(name = "english") public String english;
    }

    public static class ImageBundle {
        @Json(name = "cover") public ImageJson cover;
        @Json(name = "pages") public List<ImageJson> pages;
        @Json(name = "thumbnail") public ImageJson thumbnail;
    }

    public static class ImageJson {
        @Json(name = "h") public int height;
        @Json(name = "w") public int width;
        @Json(name = "t") public String type;
    }

    public static class Tag {
        @Json(name = "id") public int id;
        @Json(name = "name") public String name;
        @Json(name = "type") public String type;
        @Json(name = "url") public String url;
        @Json(name = "count") public int count;
    }
}
