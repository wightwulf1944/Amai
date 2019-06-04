package i.am.shiro.amai.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

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

    private Image remoteCoverImage;

    private Image remoteCoverThumbnailImage;

    private RealmList<Image> remotePageImages;

    private RealmList<Image> remotePageThumbnailImages;

    private Image localCoverImage;

    private Image localCoverThumbnailImage;

    private RealmList<Image> localPageImages;

    private RealmList<Image> localPageThumbnailImages;

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
        return isDownloaded ? localCoverImage : remoteCoverImage;
    }

    public Book setLocalCoverImage(Image localCoverImage) {
        this.localCoverImage = localCoverImage;
        return this;
    }

    public Book setRemoteCoverImage(Image coverImage) {
        this.remoteCoverImage = coverImage;
        return this;
    }

    public Image getCoverThumbnailImage() {
        return isDownloaded ? localCoverThumbnailImage : remoteCoverThumbnailImage;
    }

    public Book setLocalCoverThumbnailImage(Image localCoverThumbnailImage) {
        this.localCoverThumbnailImage = localCoverThumbnailImage;
        return this;
    }

    public Book setRemoteCoverThumbnailImage(Image coverThumbnailImage) {
        this.remoteCoverThumbnailImage = coverThumbnailImage;
        return this;
    }

    public RealmList<Image> getPageImages() {
        return isDownloaded ? localPageImages : remotePageImages;
    }

    public Book setLocalPageImages(RealmList<Image> localPageImages) {
        this.localPageImages = localPageImages;
        return this;
    }

    public Book setRemotePageImages(RealmList<Image> pageImages) {
        this.remotePageImages = pageImages;
        return this;
    }

    public RealmList<Image> getPageThumbnailImages() {
        return isDownloaded ? localPageThumbnailImages : remotePageThumbnailImages;
    }

    public Book setLocalPageThumbnailImages(RealmList<Image> localPageThumbnailImages) {
        this.localPageThumbnailImages = localPageThumbnailImages;
        return this;
    }

    public Book setRemotePageThumbnailImages(RealmList<Image> pageThumbnailImages) {
        this.remotePageThumbnailImages = pageThumbnailImages;
        return this;
    }
}
