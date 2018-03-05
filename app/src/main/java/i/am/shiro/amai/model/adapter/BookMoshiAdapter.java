package i.am.shiro.amai.model.adapter;

import com.annimon.stream.Stream;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.BookJson;
import i.am.shiro.amai.model.Image;
import io.realm.RealmList;

import static com.annimon.stream.Collectors.toCollection;
import static i.am.shiro.amai.retrofit.Nhentai.IMAGE_BASE_URL;
import static i.am.shiro.amai.retrofit.Nhentai.THUMBNAIL_BASE_URL;
import static i.am.shiro.amai.retrofit.Nhentai.WEBPAGE_BASE_URL;

/**
 * Created by Shiro on 3/5/2018.
 */

public class BookMoshiAdapter {

    @ToJson
    BookJson from(Book book) {
        // required by Moshi but should not be used
        throw new RuntimeException("Book should not be transformed back into BookJson");
    }

    @FromJson
    Book from(BookJson json) {
        return new Book.Builder()
                .setId(json.id)
                .setWebUrl(WEBPAGE_BASE_URL + json.id)
                .setTitle(json.title.english)
                .setPageCount(json.pageCount)
                .setParodyTags(getTagsByTypeFrom("parody", json))
                .setCharacterTags(getTagsByTypeFrom("character", json))
                .setGeneralTags(getTagsByTypeFrom("tag", json))
                .setArtistTags(getTagsByTypeFrom("artist", json))
                .setGroupTags(getTagsByTypeFrom("group", json))
                .setLanguageTags(getTagsByTypeFrom("language", json))
                .setCategoryTags(getTagsByTypeFrom("category", json))
                .setCoverImage(getCoverImageFrom(json))
                .setCoverThumbnailImage(getCoverThumbnailImageFrom(json))
                .setPageImages(getPageImagesFrom(json))
                .setPageThumbnailImages(getPageThumbnailImagesFrom(json))
                .make();
    }

    private RealmList<String> getTagsByTypeFrom(String type, BookJson json) {
        return Stream.of(json.tags)
                .filter(tag -> tag.type.equals(type))
                .map(tag -> tag.name)
                .collect(toCollection(RealmList::new));
    }

    private Image getCoverImageFrom(BookJson json) {
        String mediaUrl = getThumbnailImageMediaUrl(json);
        BookJson.Image cover = json.images.cover;
        return new Image.Builder()
                .setUrl(mediaUrl + "cover" + extension(cover))
                .setWidth(cover.width)
                .setHeight(cover.height)
                .make();
    }

    private Image getCoverThumbnailImageFrom(BookJson json) {
        String mediaUrl = getThumbnailImageMediaUrl(json);
        BookJson.Image coverThumbnail = json.images.thumbnail;
        return new Image.Builder()
                .setUrl(mediaUrl + "thumb" + extension(coverThumbnail))
                .setWidth(coverThumbnail.width)
                .setHeight(coverThumbnail.height)
                .make();
    }

    private RealmList<Image> getPageImagesFrom(BookJson json) {
        return Stream.range(0, json.pageCount)
                .map(index -> getPageImageFrom(index, json))
                .collect(toCollection(RealmList::new));
    }

    private RealmList<Image> getPageThumbnailImagesFrom(BookJson json) {
        return Stream.range(0, json.pageCount)
                .map(index -> getPageThumbnailImageFrom(index, json))
                .collect(toCollection(RealmList::new));
    }

    private Image getPageImageFrom(int index, BookJson json) {
        String mediaUrl = getImageMediaUrl(json);
        BookJson.Image page = json.images.pages.get(index);
        return new Image.Builder()
                .setUrl(mediaUrl + (index + 1) + extension(page))
                .setWidth(page.width)
                .setHeight(page.height)
                .make();
    }

    private Image getPageThumbnailImageFrom(int index, BookJson json) {
        String mediaUrl = getThumbnailImageMediaUrl(json);
        BookJson.Image page = json.images.pages.get(index);
        return new Image.Builder()
                .setUrl(mediaUrl + (index + 1) + "t" + extension(page))
                .setWidth(page.width)
                .setHeight(page.height)
                .make();
    }

    private String getImageMediaUrl(BookJson json) {
        return IMAGE_BASE_URL + json.mediaId + "/";
    }

    private String getThumbnailImageMediaUrl(BookJson json) {
        return THUMBNAIL_BASE_URL + json.mediaId + "/";
    }

    private String extension(BookJson.Image image) {
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
