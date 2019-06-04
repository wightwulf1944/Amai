package i.am.shiro.amai.model;

import io.realm.RealmObject;

public class Image extends RealmObject {

    private String url;

    private int width;

    private int height;

    public String getUrl() {
        return url;
    }

    public Image setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public Image setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Image setHeight(int height) {
        this.height = height;
        return this;
    }
}
