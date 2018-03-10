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

    public int getId() {
        return id;
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

    public static class Builder {
        private Book product = new Book();

        public Builder setId(int id) {
            product.id = id;
            return this;
        }

        public Builder setWebUrl(String webUrl) {
            product.webUrl = webUrl;
            return this;
        }

        public Builder setTitle(String title) {
            product.title = title;
            return this;
        }

        public Builder setPageCount(int pageCount) {
            product.pageCount = pageCount;
            return this;
        }

        public Builder setParodyTags(RealmList<String> parodyTags) {
            product.parodyTags = parodyTags;
            return this;
        }

        public Builder setCharacterTags(RealmList<String> characterTags) {
            product.characterTags = characterTags;
            return this;
        }

        public Builder setGeneralTags(RealmList<String> generalTags) {
            product.generalTags = generalTags;
            return this;
        }

        public Builder setArtistTags(RealmList<String> artistTags) {
            product.artistTags = artistTags;
            return this;
        }

        public Builder setGroupTags(RealmList<String> groupTags) {
            product.groupTags = groupTags;
            return this;
        }

        public Builder setLanguageTags(RealmList<String> languageTags) {
            product.languageTags = languageTags;
            return this;
        }

        public Builder setCategoryTags(RealmList<String> categoryTags) {
            product.categoryTags = categoryTags;
            return this;
        }

        public Builder setCoverThumbnailImage(Image coverThumbnailImage) {
            product.coverThumbnailImage = coverThumbnailImage;
            return this;
        }

        public Builder setCoverImage(Image coverImage) {
            product.coverImage = coverImage;
            return this;
        }

        public Builder setPageImages(RealmList<Image> pageImages) {
            product.pageImages = pageImages;
            return this;
        }

        public Builder setPageThumbnailImages(RealmList<Image> pageThumbnailImages) {
            product.pageThumbnailImages = pageThumbnailImages;
            return this;
        }

        public Book make() {
            return product;
        }
    }
}
