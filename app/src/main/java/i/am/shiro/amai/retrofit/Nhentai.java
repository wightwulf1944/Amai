package i.am.shiro.amai.retrofit;

import android.text.TextUtils;

import java.util.List;

import i.am.shiro.amai.model.BookJson;
import i.am.shiro.amai.model.BookSearchJson;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Shiro on 1/10/2018.
 */

public class Nhentai {

    public static Api api = new Retrofit.Builder()
            .baseUrl("https://nhentai.net/api/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(Api.class);

    public interface Api {

        @GET("gallery/{id}")
        Single<BookJson> getBookDetails(@Path("id") int id);

        @GET("gallery/{id}/related")
        Single<BookJson> getRelatedBooks(@Path("id") int id);

        @GET("galleries/search")
        Single<BookSearchJson> search(@Query("query") String query, @Query("page") Integer page);

        @GET("galleries/all")
        Single<BookSearchJson> getAll(@Query("page") Integer page);

        @GET("galleries/tagged")
        Single<BookSearchJson> getTagged(@Query("tag_id") int tagId, @Query("page") Integer page);
    }

    public static class QueryBuilder {

        private List<String> queries;

        public void addTag(String s) {
            String query = String.format("tag:\"%s\"", s);
            queries.add(query);
        }

        public void addArtist(String s) {
            String query = String.format("artist:\"%s\"", s);
            queries.add(query);
        }

        public void addLanguage(String s) {
            String query = String.format("language:\"%s\"", s);
            queries.add(query);
        }

        public String build() {
            return TextUtils.join(" ", queries);
        }
    }
}
