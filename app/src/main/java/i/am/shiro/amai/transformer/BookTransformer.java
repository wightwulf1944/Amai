package i.am.shiro.amai.transformer;

import com.annimon.stream.Stream;

import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.BookJson;
import i.am.shiro.amai.model.Image;
import i.am.shiro.amai.retrofit.Nhentai;
import io.realm.RealmList;

import static com.annimon.stream.Collectors.toCollection;
import static i.am.shiro.amai.retrofit.Nhentai.IMAGE_BASE_URL;
import static i.am.shiro.amai.retrofit.Nhentai.THUMBNAIL_BASE_URL;

public class BookTransformer {

    public static Book transform(BookJson json) {
        return new Book()
                .setId(json.id)
                .setWebUrl(Nhentai.WEBPAGE_BASE_URL + json.id)
                .setTitle(json.title.english)
                .setPageCount(json.pageCount)
                .setParodyTags(parodyTagsOf(json))
                .setCharacterTags(characterTagsOf(json))
                .setGeneralTags(generalTagsOf(json))
                .setArtistTags(artistTagsOf(json))
                .setGroupTags(groupTagsOf(json))
                .setLanguageTags(languageTagsOf(json))
                .setCategoryTags(categoryTagsOf(json))
                .setCoverImage(coverImageOf(json))
                .setCoverThumbnailImage(coverThumbnailImageOf(json))
                .setPageImages(pageImagesOf(json))
                .setPageThumbnailImages(pageThumbnailImagesOf(json));
    }

    private static RealmList<String> parodyTagsOf(BookJson json) {
        return getTagsByType(json, "parody");
    }

    private static RealmList<String> characterTagsOf(BookJson json) {
        return getTagsByType(json, "character");
    }

    private static RealmList<String> generalTagsOf(BookJson json) {
        return getTagsByType(json, "tag");
    }

    private static RealmList<String> artistTagsOf(BookJson json) {
        return getTagsByType(json, "artist");
    }

    private static RealmList<String> groupTagsOf(BookJson json) {
        return getTagsByType(json, "group");
    }

    private static RealmList<String> languageTagsOf(BookJson json) {
        return getTagsByType(json, "language");
    }

    private static RealmList<String> categoryTagsOf(BookJson json) {
        return getTagsByType(json, "category");
    }

    private static RealmList<String> getTagsByType(BookJson json, String type) {
        return Stream.of(json.tags)
                .filter(tag -> tag.type.equals(type))
                .map(tag -> tag.name)
                .collect(toCollection(RealmList::new));
    }

    private static Image coverImageOf(BookJson json) {
        String mediaPath = smallImageMediaPathOf(json);
        BookJson.ImageJson cover = json.images.cover;
        return imageOf(mediaPath + "cover", cover);
    }

    private static Image coverThumbnailImageOf(BookJson json) {
        String mediaPath = smallImageMediaPathOf(json);
        BookJson.ImageJson coverThumbnail = json.images.thumbnail;
        return imageOf(mediaPath + "thumb", coverThumbnail);
    }

    private static RealmList<Image> pageImagesOf(BookJson json) {
        RealmList<Image> images = new RealmList<>();
        String mediaPath = largeImageMediaPathOf(json);
        for (int i = 0; i < json.pageCount; i++) {
            BookJson.ImageJson page = json.images.pages.get(i);
            images.add(imageOf(mediaPath + (i + 1), page));
        }
        return images;
    }

    private static RealmList<Image> pageThumbnailImagesOf(BookJson json) {
        RealmList<Image> images = new RealmList<>();
        String mediaPath = smallImageMediaPathOf(json);
        for (int i = 0; i < json.pageCount; i++) {
            BookJson.ImageJson page = json.images.pages.get(i);
            images.add(imageOf(mediaPath + (i + 1) + "t", page));
        }
        return images;
    }

    private static Image imageOf(String url, BookJson.ImageJson imageJson) {
        return new Image()
                .setUrl(url + extensionOf(imageJson))
                .setWidth(imageJson.width)
                .setHeight(imageJson.height);
    }

    private static String smallImageMediaPathOf(BookJson json) {
        return THUMBNAIL_BASE_URL + json.mediaId + "/";
    }

    private static String largeImageMediaPathOf(BookJson json) {
        return IMAGE_BASE_URL + json.mediaId + "/";
    }

    private static String extensionOf(BookJson.ImageJson imageJson) {
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
