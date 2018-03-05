package i.am.shiro.amai.model;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import i.am.shiro.amai.constant.BookStatus;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static i.am.shiro.amai.retrofit.Nhentai.WEBPAGE_BASE_URL;

/**
 * Created by Shiro on 1/6/2018.
 */

public class Book extends RealmObject {

    @PrimaryKey
    private int id;

    @BookStatus
    private int status;

    private String webUrl;

    private String title;

    private int pageCount;

    private RealmList<String> parodyTags;

    private RealmList<String> characterTags;

    private RealmList<String> generalTags;

    private RealmList<String> artistTags;

    private RealmList<String> groupTags;

    private RealmList<String> languageTags;

    private RealmList<String> categoryTags;

    private String previewUrl;

    private int previewWidth;

    private int previewHeight;

    private String coverUrl;

    private int coverWidth;

    private int coverHeight;

    private RealmList<String> pageThumbnailUrls;

    private RealmList<String> pageUrls;

    public Book() {
        // realm required default constructor
    }

    private Book(BookJson json) {
        id = json.id;
        webUrl = WEBPAGE_BASE_URL + json.id;
        title = json.title.english;
        pageCount = json.pageCount;
        parodyTags = json.getTagsByType("parody");
        characterTags = json.getTagsByType("character");
        generalTags = json.getTagsByType("tag");
        artistTags = json.getTagsByType("artist");
        groupTags = json.getTagsByType("group");
        languageTags = json.getTagsByType("language");
        categoryTags = json.getTagsByType("category");
        previewUrl = json.getPreviewUrl();
        previewWidth = json.images.thumbnail.width;
        previewHeight = json.images.thumbnail.height;
        coverUrl = json.getCoverUrl();
        coverWidth = json.images.cover.width;
        coverHeight = json.images.cover.height;
        pageThumbnailUrls = json.getPageThumbnailUrls();
        pageUrls = json.getPageUrls();
    }

    public int getId() {
        return id;
    }

    @BookStatus
    public int getStatus() {
        return status;
    }

    public void setStatus(@BookStatus int status) {
        this.status = status;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getPageCountStr() {
        return String.valueOf(pageCount);
    }

    public RealmList<String> getParodyTags() {
        return parodyTags;
    }

    public RealmList<String> getCharacterTags() {
        return characterTags;
    }

    public RealmList<String> getGeneralTags() {
        return generalTags;
    }

    public RealmList<String> getArtistTags() {
        return artistTags;
    }

    public RealmList<String> getGroupTags() {
        return groupTags;
    }

    public RealmList<String> getLanguageTags() {
        return languageTags;
    }

    public RealmList<String> getCategoryTags() {
        return categoryTags;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public int getPreviewWidth() {
        return previewWidth;
    }

    public int getPreviewHeight() {
        return previewHeight;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public int getCoverWidth() {
        return coverWidth;
    }

    public int getCoverHeight() {
        return coverHeight;
    }

    public RealmList<String> getPageThumbnailUrls() {
        return pageThumbnailUrls;
    }

    public RealmList<String> getPageUrls() {
        return pageUrls;
    }

    public static class MoshiAdapter {

        @FromJson
        Book from(BookJson json) {
            return new Book(json);
        }

        @ToJson
        BookJson from(Book book) {
            // required by Moshi but should not be used
            return null;
        }
    }
}
