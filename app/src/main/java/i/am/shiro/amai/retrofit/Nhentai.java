package i.am.shiro.amai.retrofit;

import android.support.annotation.StringDef;

import com.squareup.moshi.Moshi;

import java.lang.annotation.Retention;

import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.BookSearchJson;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static i.am.shiro.amai.retrofit.Nhentai.SortOrder.DATE;
import static i.am.shiro.amai.retrofit.Nhentai.SortOrder.POPULAR;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by Shiro on 1/10/2018.
 */

public class Nhentai {

    public static final String WEBPAGE_BASE_URL = "https://nhentai.net/g/";

    public static final String THUMBNAIL_BASE_URL = "https://t.nhentai.net/galleries/";

    public static final String IMAGE_BASE_URL = "https://i.nhentai.net/galleries/";

    private static final String API_URL = "https://nhentai.net/api/";

    public static final Api api = buildApi();

    private static Api buildApi() {
        RxJava2CallAdapterFactory callAdapterFactory = RxJava2CallAdapterFactory.createAsync();

        Moshi moshi = new Moshi.Builder()
                .add(new BookMoshiAdapter())
                .build();

        MoshiConverterFactory converterFactory = MoshiConverterFactory.create(moshi);

        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(converterFactory)
                .build()
                .create(Api.class);
    }

    public interface Api {

        @GET("gallery/{id}")
        Single<Book> getBookDetails(
                @Path("id") int id);

        @GET("gallery/{id}/related")
        Single<Book> getRelatedBooks(
                @Path("id") int id);

        @GET("galleries/search")
        Single<BookSearchJson> search(
                @Query("query") String query,
                @Query("page") Integer page,
                @Query("sort") @SortOrder String sortOrder);

        @GET("galleries/all")
        Single<BookSearchJson> getAll(
                @Query("page") Integer page);

        @GET("galleries/tagged")
        Single<BookSearchJson> getTagById(
                @Query("tag_id") int tagId,
                @Query("page") Integer page,
                @Query("sort") @SortOrder String sortOrder);
    }

    @Retention(SOURCE)
    @StringDef({POPULAR, DATE})
    public @interface SortOrder {
        String POPULAR = "popular";
        String DATE = "date";
    }

    public static class QueryBuilder {

        private final StringBuilder sb = new StringBuilder();

        public void addTag(String s) {
            String query = String.format("tag:\"%s\"", s);
            sb.append(query);
        }

        public void addArtist(String s) {
            String query = String.format("artist:\"%s\"", s);
            sb.append(query);
        }

        public void addLanguage(String s) {
            String query = String.format("language:\"%s\"", s);
            sb.append(query);
        }

        public String build() {
            return sb.toString();
        }
    }
}
