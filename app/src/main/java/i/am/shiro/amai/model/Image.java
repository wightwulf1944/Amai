package i.am.shiro.amai.model;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

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
}
