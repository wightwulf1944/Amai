package i.am.shiro.amai.model;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by Shiro on 1/10/2018.
 */

public class BookSearchJson {
    @Json(name = "result") public List<BookJson> results;
    @Json(name = "num_pages") int pageTotal;
    @Json(name = "per_page") int booksPerPage;
}
