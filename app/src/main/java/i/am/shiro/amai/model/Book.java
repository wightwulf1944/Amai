package i.am.shiro.amai.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Shiro on 1/6/2018.
 */

public class Book extends RealmObject {

    @PrimaryKey
    private int id;

    private boolean isDownloaded;

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

    private Image coverImage;

    private Image coverThumbnailImage;

    private RealmList<Image> pageImages;

    private RealmList<Image> pageThumbnailImages;

    public Book() {
        // Realm required no argument constructor
    }

    public Book(Adapter adapter) {
        id = adapter.getId();
        webUrl = adapter.getWebUrl();
        title = adapter.getTitle();
        pageCount = adapter.getPageCount();
        parodyTags = adapter.getParodyTags();
        characterTags = adapter.getCharacterTags();
        generalTags = adapter.getGeneralTags();
        artistTags = adapter.getArtistTags();
        groupTags = adapter.getGroupTags();
        languageTags = adapter.getLanguageTags();
        categoryTags = adapter.getCategoryTags();
        coverThumbnailImage = adapter.getCoverThumbnailImage();
        coverImage = adapter.getCoverImage();
        pageImages = adapter.getPageImages();
        pageThumbnailImages = adapter.getPageThumbnailImages();
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public int getId() {
        return id;
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

    public Image getCoverImage() {
        return coverImage;
    }

    public Image getCoverThumbnailImage() {
        return coverThumbnailImage;
    }

    public RealmList<Image> getPageImages() {
        return pageImages;
    }

    public RealmList<Image> getPageThumbnailImages() {
        return pageThumbnailImages;
    }

    public interface Adapter {
        int getId();

        String getWebUrl();

        String getTitle();

        int getPageCount();

        RealmList<String> getParodyTags();

        RealmList<String> getCharacterTags();

        RealmList<String> getGeneralTags();

        RealmList<String> getArtistTags();

        RealmList<String> getGroupTags();

        RealmList<String> getLanguageTags();

        RealmList<String> getCategoryTags();

        Image getCoverThumbnailImage();

        Image getCoverImage();

        RealmList<Image> getPageImages();

        RealmList<Image> getPageThumbnailImages();
    }
}
