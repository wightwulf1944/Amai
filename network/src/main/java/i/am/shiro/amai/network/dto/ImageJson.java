package i.am.shiro.amai.network.dto;

import com.squareup.moshi.Json;

public class ImageJson {
    @Json(name = "h")
    public int height;
    @Json(name = "w")
    public int width;
    @Json(name = "t")
    public String type;
}
