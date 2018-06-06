package i.am.shiro.amai.transformer;

import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.BookJson;
import i.am.shiro.amai.model.Image;
import i.am.shiro.amai.retrofit.Nhentai;
import io.realm.RealmList;

import static i.am.shiro.amai.retrofit.Nhentai.IMAGE_BASE_URL;
import static i.am.shiro.amai.retrofit.Nhentai.THUMBNAIL_BASE_URL;

/**
 * Responsible for transforming BookJson into Book
 */
public class BookTransformer {

    public static Book transform(BookJson json) {
        SortedTags sortedTags = new SortedTags(json);
        return new Book()
                .setId(json.id)
                .setWebUrl(Nhentai.WEBPAGE_BASE_URL + json.id)
                .setTitle(json.title.english)
                .setPageCount(json.pageCount)
                .setParodyTags(sortedTags.parodyTags)
                .setCharacterTags(sortedTags.characterTags)
                .setGeneralTags(sortedTags.generalTags)
                .setArtistTags(sortedTags.artistTags)
                .setGroupTags(sortedTags.groupTags)
                .setLanguageTags(sortedTags.languageTags)
                .setCategoryTags(sortedTags.categoryTags)
                .setCoverImage(coverImageOf(json))
                .setCoverThumbnailImage(coverThumbnailImageOf(json))
                .setPageImages(pageImagesOf(json))
                .setPageThumbnailImages(pageThumbnailImagesOf(json));
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

    private static class SortedTags {

        private final RealmList<String> parodyTags = new RealmList<>();
        private final RealmList<String> characterTags = new RealmList<>();
        private final RealmList<String> generalTags = new RealmList<>();
        private final RealmList<String> artistTags = new RealmList<>();
        private final RealmList<String> groupTags = new RealmList<>();
        private final RealmList<String> languageTags = new RealmList<>();
        private final RealmList<String> categoryTags = new RealmList<>();

        private SortedTags(BookJson json) {
            for (BookJson.Tag tag : json.tags) {
                getListOfType(tag.type).add(tag.name);
            }
        }

        private RealmList<String> getListOfType(String type) {
            switch (type) {
                case "parody":
                    return parodyTags;
                case "character":
                    return characterTags;
                case "tag":
                    return generalTags;
                case "artist":
                    return artistTags;
                case "group":
                    return groupTags;
                case "language":
                    return languageTags;
                case "category":
                    return categoryTags;
                default:
                    throw new IllegalArgumentException(type);
            }
        }
    }
}
