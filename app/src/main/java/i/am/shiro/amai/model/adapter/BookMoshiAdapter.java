package i.am.shiro.amai.model.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.BookJson;

/**
 * Created by Shiro on 3/5/2018.
 */

public class BookMoshiAdapter {

    @ToJson
    BookJson from(Book book) {
        // required by Moshi but should not be used
        throw new RuntimeException("Book should not be transformed back into BookJson");
    }

    @FromJson
    Book from(BookJson json) {
        return new Book(json.getBookAdapter());
    }
}
