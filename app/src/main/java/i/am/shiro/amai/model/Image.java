package i.am.shiro.amai.model;

import io.realm.RealmObject;

/**
 * Created by Shiro on 3/5/2018.
 */

public class Image extends RealmObject {

    private String url;

    private int width;

    private int height;

    public String getUrl() {
        return url;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static class Builder {
        private Image product = new Image();

        public Builder setUrl(String url) {
            product.url = url;
            return this;
        }

        public Builder setWidth(int width) {
            product.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            product.height = height;
            return this;
        }

        public Image make() {
            return product;
        }
    }
}
