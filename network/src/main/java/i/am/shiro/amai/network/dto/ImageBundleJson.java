package i.am.shiro.amai.network.dto;

import com.squareup.moshi.Json;

import java.util.List;

public class ImageBundleJson {
    @Json(name = "cover")
    public ImageJson cover;
    @Json(name = "pages")
    public List<ImageJson> pages;
    @Json(name = "thumbnail")
    public ImageJson thumbnail;
}
