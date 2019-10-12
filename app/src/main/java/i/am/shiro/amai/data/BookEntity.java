package i.am.shiro.amai.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import i.am.shiro.amai.network.Nhentai;
import i.am.shiro.amai.network.dto.BookJson;

@Entity
public class BookEntity {

    @PrimaryKey
    private int id;

    private String webUrl;

    private String title;

    private int pageCount;

    private int coverThumbnailImageWidth;

    private int coverThumbnailImageHeight;

    private String coverThumbnailImageUrl;

    private int coverImageWidth;

    private int coverImageHeight;

    private String coverImageUrl;

    public BookEntity(int id, String webUrl, String title, int pageCount,
                      int coverThumbnailImageWidth, int coverThumbnailImageHeight,
                      String coverThumbnailImageUrl, int coverImageWidth, int coverImageHeight,
                      String coverImageUrl) {
        this.id = id;
        this.webUrl = webUrl;
        this.title = title;
        this.pageCount = pageCount;
        this.coverThumbnailImageWidth = coverThumbnailImageWidth;
        this.coverThumbnailImageHeight = coverThumbnailImageHeight;
        this.coverThumbnailImageUrl = coverThumbnailImageUrl;
        this.coverImageWidth = coverImageWidth;
        this.coverImageHeight = coverImageHeight;
        this.coverImageUrl = coverImageUrl;
    }

    public BookEntity(BookJson json) {
        JsonAdapter adapter = new JsonAdapter(json);
        id = adapter.getId();
        webUrl = adapter.getWebUrl();
        title = adapter.getTitle();
        pageCount = adapter.getPageCount();
        coverThumbnailImageWidth = adapter.getCoverThumbnailImageWidth();
        coverThumbnailImageHeight = adapter.getCoverThumbnailImageHeight();
        coverThumbnailImageUrl = adapter.getCoverThumbnailImageUrl();
        coverImageWidth = adapter.getCoverImageWidth();
        coverImageHeight = adapter.getCoverImageHeight();
        coverImageUrl = adapter.getCoverImageUrl();
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

    public int getPageCount() {
        return pageCount;
    }

    public int getCoverThumbnailImageWidth() {
        return coverThumbnailImageWidth;
    }

    public int getCoverThumbnailImageHeight() {
        return coverThumbnailImageHeight;
    }

    public String getCoverThumbnailImageUrl() {
        return coverThumbnailImageUrl;
    }

    public int getCoverImageWidth() {
        return coverImageWidth;
    }

    public int getCoverImageHeight() {
        return coverImageHeight;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    private static final class JsonAdapter {

        private final BookJson json;

        private JsonAdapter(BookJson json) {
            this.json = json;
        }

        private int getId() {
            return json.id;
        }

        private String getWebUrl() {
            return Nhentai.WEBPAGE_BASE_URL + json.id;
        }

        private String getTitle() {
            return json.title.english;
        }

        private int getPageCount() {
            return json.pageCount;
        }

        private int getCoverThumbnailImageWidth() {
            return json.images.thumbnail.width;
        }

        private int getCoverThumbnailImageHeight() {
            return json.images.thumbnail.height;
        }

        private String getCoverThumbnailImageUrl() {
            String type = json.images.thumbnail.type;
            return Nhentai.THUMBNAIL_BASE_URL + json.mediaId + "/thumb" + extensionFromType(type);
        }

        private int getCoverImageWidth() {
            return json.images.cover.width;
        }

        private int getCoverImageHeight() {
            return json.images.cover.height;
        }

        private String getCoverImageUrl() {
            String type = json.images.cover.type;
            return Nhentai.THUMBNAIL_BASE_URL + json.mediaId + "/cover" + extensionFromType(type);
        }

// TODO recycle this
//        private List<String> getTags(String type) {
//            return Stream.of(json.tags)
//                    .filter(tag -> tag.type.equals(type))
//                    .map(tag -> tag.name)
//                    .toList();
//        }

        private static String extensionFromType(String type) {
            switch (type) {
                case "j":
                    return ".jpg";
                case "p":
                    return ".png";
                case "g":
                    return ".gif";
                default:
                    throw new RuntimeException("Unknown type " + type);
            }
        }
    }
}
