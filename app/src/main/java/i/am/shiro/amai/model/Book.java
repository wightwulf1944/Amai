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

    public Book setId(int id) {
        this.id = id;
        return this;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public Book setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
        return this;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public Book setWebUrl(String webUrl) {
        this.webUrl = webUrl;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Book setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getPageCount() {
        return pageCount;
    }

    public Book setPageCount(int pageCount) {
        this.pageCount = pageCount;
        return this;
    }

    public RealmList<String> getParodyTags() {
        return parodyTags;
    }

    public Book setParodyTags(RealmList<String> parodyTags) {
        this.parodyTags = parodyTags;
        return this;
    }

    public RealmList<String> getCharacterTags() {
        return characterTags;
    }

    public Book setCharacterTags(RealmList<String> characterTags) {
        this.characterTags = characterTags;
        return this;
    }

    public RealmList<String> getGeneralTags() {
        return generalTags;
    }

    public Book setGeneralTags(RealmList<String> generalTags) {
        this.generalTags = generalTags;
        return this;
    }

    public RealmList<String> getArtistTags() {
        return artistTags;
    }

    public Book setArtistTags(RealmList<String> artistTags) {
        this.artistTags = artistTags;
        return this;
    }

    public RealmList<String> getGroupTags() {
        return groupTags;
    }

    public Book setGroupTags(RealmList<String> groupTags) {
        this.groupTags = groupTags;
        return this;
    }

    public RealmList<String> getLanguageTags() {
        return languageTags;
    }

    public Book setLanguageTags(RealmList<String> languageTags) {
        this.languageTags = languageTags;
        return this;
    }

    public RealmList<String> getCategoryTags() {
        return categoryTags;
    }

    public Book setCategoryTags(RealmList<String> categoryTags) {
        this.categoryTags = categoryTags;
        return this;
    }

    public Image getCoverImage() {
        return coverImage;
    }

    public Book setCoverImage(Image coverImage) {
        this.coverImage = coverImage;
        return this;
    }

    public Image getCoverThumbnailImage() {
        return coverThumbnailImage;
    }

    public Book setCoverThumbnailImage(Image coverThumbnailImage) {
        this.coverThumbnailImage = coverThumbnailImage;
        return this;
    }

    public RealmList<Image> getPageImages() {
        return pageImages;
    }

    public Book setPageImages(RealmList<Image> pageImages) {
        this.pageImages = pageImages;
        return this;
    }

    public RealmList<Image> getPageThumbnailImages() {
        return pageThumbnailImages;
    }

    public Book setPageThumbnailImages(RealmList<Image> pageThumbnailImages) {
        this.pageThumbnailImages = pageThumbnailImages;
        return this;
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
    public void updateFrom(Book sourceBook) {
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
