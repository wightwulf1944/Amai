package i.am.shiro.amai.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Book;
import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by Shiro on 1/20/2018.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String BOOK_ID = "bookId";

    private Realm realm;

    public static Intent makeIntent(Context context, int bookId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(BOOK_ID, bookId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        int bookId = getIntent().getIntExtra(BOOK_ID, -1);
        Book book = getBook(bookId);
        Timber.d("Detail activity received book with id: %s", book.getId());

        setContentView(R.layout.activity_detail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private Book getBook(int bookId) {
        return realm.where(Book.class)
                .equalTo("id", bookId)
                .findFirst();
    }
}
