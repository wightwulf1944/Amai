package i.am.shiro.amai.network.dto;

import com.squareup.moshi.Json;

public final class TitleJson {
    @Json(name = "japanese")
    public String japanese;
    @Json(name = "pretty")
    public String pretty;
    @Json(name = "english")
    public String english;
}
