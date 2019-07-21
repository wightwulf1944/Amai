package i.am.shiro.amai.network.dto;

import com.squareup.moshi.Json;

public class TagJson {
    @Json(name = "id")
    public int id;
    @Json(name = "name")
    public String name;
    @Json(name = "type")
    public String type;
    @Json(name = "url")
    public String url;
    @Json(name = "count")
    public int count;
}
