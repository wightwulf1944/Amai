package i.am.shiro.amai.network.dto;

import com.squareup.moshi.Json;

import java.util.List;

public class SearchResponseJson {
    @Json(name = "result")
    public List<BookJson> results;
    @Json(name = "num_pages")
    int pageTotal;
    @Json(name = "per_page")
    int booksPerPage;
}
