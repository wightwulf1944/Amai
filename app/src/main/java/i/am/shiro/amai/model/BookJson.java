package i.am.shiro.amai.model;

import com.annimon.stream.Stream;
import com.squareup.moshi.Json;

import java.util.List;

import io.realm.RealmList;

import static com.annimon.stream.Collectors.toCollection;
import static i.am.shiro.amai.retrofit.Nhentai.IMAGE_BASE_URL;
import static i.am.shiro.amai.retrofit.Nhentai.THUMBNAIL_BASE_URL;

/**
 * Created by Shiro on 1/10/2018.
 */

class BookJson {
    @Json(name = "id") int id;
    @Json(name = "media_id") String mediaId;
    @Json(name = "upload_date") long uploadDate;
    @Json(name = "num_favorites") int favCount;
    @Json(name = "num_pages") int pageCount;
    @Json(name = "scanlator") String scanlator;
    @Json(name = "title") Title title;
    @Json(name = "tags") List<Tag> tags;
    @Json(name = "images") ImageBundle images;

    static class Title {
        @Json(name = "japanese") String japanese;
        @Json(name = "pretty") String pretty;
        @Json(name = "english") String english;
    }

    static class ImageBundle {
        @Json(name = "cover") Image cover;
        @Json(name = "pages") List<Image> pages;
        @Json(name = "thumbnail") Image thumbnail;
    }

    static class Image {
        @Json(name = "h") int height;
        @Json(name = "w") int width;
        @Json(name = "t") String type;
    }

    static class Tag {
        @Json(name = "id") int id;
        @Json(name = "name") String name;
        @Json(name = "type") String type;
        @Json(name = "url") String url;
        @Json(name = "count") int count;
    }

    RealmList<String> getTagsByType(String type) {
        return Stream.of(tags)
                .filter(tag -> tag.type.equals(type))
                .map(tag -> tag.name)
                .collect(toCollection(RealmList::new));
    }

    String getCoverUrl() {
        return getThumbnailMediaUrl() + "cover" + extension(images.cover);
    }

    String getPreviewUrl() {
        return getThumbnailMediaUrl() + "thumb" + extension(images.thumbnail);
    }

    RealmList<String> getPageThumbnailUrls() {
        String mediaUrl = getThumbnailMediaUrl();
        return Stream.of(images.pages)
                .mapIndexed((index, image) -> mediaUrl + (index + 1) + "t" + extension(image))
                .collect(toCollection(RealmList::new));
    }

    RealmList<String> getPageUrls() {
        String mediaUrl = getImageMediaUrl();
        return Stream.of(images.pages)
                .mapIndexed((index, image) -> mediaUrl + (index + 1) + extension(image))
                .collect(toCollection(RealmList::new));
    }

    private String getThumbnailMediaUrl() {
        return THUMBNAIL_BASE_URL + mediaId + "/";
    }

    private String getImageMediaUrl() {
        return IMAGE_BASE_URL + mediaId + "/";
    }

    private static String extension(Image image) {
        switch (image.type) {
            case "j":
                return ".jpg";
            case "p":
                return ".png";
            default:
                throw new RuntimeException("Unknown type " + image.type);
        }
    }
}
