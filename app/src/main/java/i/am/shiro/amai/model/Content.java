package i.am.shiro.amai.model;

/**
 * Created by Shiro on 1/6/2018.
 */

public class Content {

    private final String title;

    private String source;

    private String artist;

    private final int pageCount = 123;

    public Content(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getPageCountStr() {
        return String.valueOf(pageCount);
    }

    public String getArtist() {
        return artist;
    }
}
