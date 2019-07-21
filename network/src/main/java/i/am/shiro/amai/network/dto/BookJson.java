package i.am.shiro.amai.network.dto;

import com.squareup.moshi.Json;

import java.util.List;

public class BookJson {
    @Json(name = "id")
    public int id;
    @Json(name = "media_id")
    public String mediaId;
    @Json(name = "upload_date")
    public long uploadDate;
    @Json(name = "num_favorites")
    public int favCount;
    @Json(name = "num_pages")
    public int pageCount;
    @Json(name = "scanlator")
    public String scanlator;
    @Json(name = "title")
    public TitleJson title;
    @Json(name = "tags")
    public List<TagJson> tags;
    @Json(name = "images")
    public ImageBundleJson images;

}
