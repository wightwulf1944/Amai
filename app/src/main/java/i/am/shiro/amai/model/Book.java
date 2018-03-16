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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public RealmList<String> getParodyTags() {
        return parodyTags;
    }

    public void setParodyTags(RealmList<String> parodyTags) {
        this.parodyTags = parodyTags;
    }

    public RealmList<String> getCharacterTags() {
        return characterTags;
    }

    public void setCharacterTags(RealmList<String> characterTags) {
        this.characterTags = characterTags;
    }

    public RealmList<String> getGeneralTags() {
        return generalTags;
    }

    public void setGeneralTags(RealmList<String> generalTags) {
        this.generalTags = generalTags;
    }

    public RealmList<String> getArtistTags() {
        return artistTags;
    }

    public void setArtistTags(RealmList<String> artistTags) {
        this.artistTags = artistTags;
    }

    public RealmList<String> getGroupTags() {
        return groupTags;
    }

    public void setGroupTags(RealmList<String> groupTags) {
        this.groupTags = groupTags;
    }

    public RealmList<String> getLanguageTags() {
        return languageTags;
    }

    public void setLanguageTags(RealmList<String> languageTags) {
        this.languageTags = languageTags;
    }

    public RealmList<String> getCategoryTags() {
        return categoryTags;
    }

    public void setCategoryTags(RealmList<String> categoryTags) {
        this.categoryTags = categoryTags;
    }

    public Image getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Image coverImage) {
        this.coverImage = coverImage;
    }

    public Image getCoverThumbnailImage() {
        return coverThumbnailImage;
    }

    public void setCoverThumbnailImage(Image coverThumbnailImage) {
        this.coverThumbnailImage = coverThumbnailImage;
    }

    public RealmList<Image> getPageImages() {
        return pageImages;
    }

    public void setPageImages(RealmList<Image> pageImages) {
        this.pageImages = pageImages;
    }

    public RealmList<Image> getPageThumbnailImages() {
        return pageThumbnailImages;
    }

    public void setPageThumbnailImages(RealmList<Image> pageThumbnailImages) {
        this.pageThumbnailImages = pageThumbnailImages;
    }

    /**
     * Convenience method
     */
    public String getPageCountStr() {
        return String.valueOf(pageCount);
    }

    /**
     * Updates some of this book's fields with the values from sourceBook.
     * <p>
     * If this book is a managed RealmObject, this method must be called
     * within a Realm transaction
     *
     * @param sourceBook
     */
    public void mergeWith(Book sourceBook) {
        webUrl = sourceBook.webUrl;
        title = sourceBook.title;
        pageCount = sourceBook.pageCount;
        parodyTags = sourceBook.parodyTags;
        characterTags = sourceBook.characterTags;
        generalTags = sourceBook.generalTags;
        artistTags = sourceBook.artistTags;
        groupTags = sourceBook.groupTags;
        languageTags = sourceBook.languageTags;
        categoryTags = sourceBook.categoryTags;
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
