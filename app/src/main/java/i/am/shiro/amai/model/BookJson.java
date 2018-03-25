package i.am.shiro.amai.model;

import com.annimon.stream.Stream;
import com.squareup.moshi.Json;

import java.util.List;

import io.realm.RealmList;

import static com.annimon.stream.Collectors.toCollection;
import static i.am.shiro.amai.retrofit.Nhentai.IMAGE_BASE_URL;
import static i.am.shiro.amai.retrofit.Nhentai.THUMBNAIL_BASE_URL;
import static i.am.shiro.amai.retrofit.Nhentai.WEBPAGE_BASE_URL;

/**
 * Created by Shiro on 1/10/2018.
 */

public class BookJson {
    @Json(name = "id") private int id;
    @Json(name = "media_id") private String mediaId;
    @Json(name = "upload_date") private long uploadDate;
    @Json(name = "num_favorites") private int favCount;
    @Json(name = "num_pages") private int pageCount;
    @Json(name = "scanlator") private String scanlator;
    @Json(name = "title") private Title title;
    @Json(name = "tags") private List<Tag> tags;
    @Json(name = "images") private ImageBundle images;

    private static class Title {
        @Json(name = "japanese") private String japanese;
        @Json(name = "pretty") private String pretty;
        @Json(name = "english") private String english;
    }

    private static class ImageBundle {
        @Json(name = "cover") private ImageJson cover;
        @Json(name = "pages") private List<ImageJson> pages;
        @Json(name = "thumbnail") private ImageJson thumbnail;
    }

    private static class ImageJson {
        @Json(name = "h") private int height;
        @Json(name = "w") private int width;
        @Json(name = "t") private String type;
    }

    private static class Tag {
        @Json(name = "id") private int id;
        @Json(name = "name") private String name;
        @Json(name = "type") private String type;
        @Json(name = "url") private String url;
        @Json(name = "count") private int count;
    }

    public Book.Adapter getBookAdapter() {
        return new BookAdapter();
    }

    private class BookAdapter implements Book.Adapter {

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getWebUrl() {
            return WEBPAGE_BASE_URL + id;
        }

        @Override
        public String getTitle() {
            return title.english;
        }

        @Override
        public int getPageCount() {
            return pageCount;
        }

        @Override
        public RealmList<String> getParodyTags() {
            return getTagsByType("parody");
        }

        @Override
        public RealmList<String> getCharacterTags() {
            return getTagsByType("character");
        }

        @Override
        public RealmList<String> getGeneralTags() {
            return getTagsByType("tag");
        }

        @Override
        public RealmList<String> getArtistTags() {
            return getTagsByType("artist");
        }

        @Override
        public RealmList<String> getGroupTags() {
            return getTagsByType("group");
        }

        @Override
        public RealmList<String> getLanguageTags() {
            return getTagsByType("language");
        }

        @Override
        public RealmList<String> getCategoryTags() {
            return getTagsByType("category");
        }

        @Override
        public Image getCoverImage() {
            String mediaUrl = getThumbnailImageMediaUrl();
            ImageJson cover = images.cover;
            return new Image()
                    .setUrl(mediaUrl + "cover" + extension(cover))
                    .setWidth(cover.width)
                    .setHeight(cover.height);
        }

        @Override
        public Image getCoverThumbnailImage() {
            String mediaUrl = getThumbnailImageMediaUrl();
            ImageJson coverThumbnail = images.thumbnail;
            return new Image()
                    .setUrl(mediaUrl + "thumb" + extension(coverThumbnail))
                    .setWidth(coverThumbnail.width)
                    .setHeight(coverThumbnail.height);
        }

        @Override
        public RealmList<Image> getPageImages() {
            return Stream.range(0, pageCount)
                    .map(this::getPageImageFrom)
                    .collect(toCollection(RealmList::new));
        }

        @Override
        public RealmList<Image> getPageThumbnailImages() {
            return Stream.range(0, pageCount)
                    .map(this::getPageThumbnailImageFrom)
                    .collect(toCollection(RealmList::new));
        }

        private RealmList<String> getTagsByType(String type) {
            return Stream.of(tags)
                    .filter(tag -> tag.type.equals(type))
                    .map(tag -> tag.name)
                    .collect(toCollection(RealmList::new));
        }

        private Image getPageImageFrom(int index) {
            String mediaUrl = getImageMediaUrl();
            ImageJson page = images.pages.get(index);
            return new Image()
                    .setUrl(mediaUrl + (index + 1) + extension(page))
                    .setWidth(page.width)
                    .setHeight(page.height);
        }

        private Image getPageThumbnailImageFrom(int index) {
            String mediaUrl = getThumbnailImageMediaUrl();
            ImageJson page = images.pages.get(index);
            return new Image()
                    .setUrl(mediaUrl + (index + 1) + "t" + extension(page))
                    .setWidth(page.width)
                    .setHeight(page.height);
        }

        private String getImageMediaUrl() {
            return IMAGE_BASE_URL + mediaId + "/";
        }

        private String getThumbnailImageMediaUrl() {
            return THUMBNAIL_BASE_URL + mediaId + "/";
        }

        private String extension(ImageJson imageJson) {
            switch (imageJson.type) {
                case "j":
                    return ".jpg";
                case "p":
                    return ".png";
                case "g":
                    return ".gif";
                default:
                    throw new RuntimeException("Unknown type " + imageJson.type);
            }
        }
    }
}
