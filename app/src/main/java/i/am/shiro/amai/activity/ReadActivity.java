package i.am.shiro.amai.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.BookPageAdapter;
import i.am.shiro.amai.model.Book;
import io.realm.Realm;

public class ReadActivity extends AppCompatActivity {

    private static final String BOOK_ID = "bookId";

    private static final String PAGE_INDEX = "pageIndex";

    private final Realm realm = Realm.getDefaultInstance();

    @NonNull
    public static Intent makeIntent(Context context, Book book, int pageIndex) {
        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra(BOOK_ID, book.getId());
        intent.putExtra(PAGE_INDEX, pageIndex);
        return intent;
    }

    @NonNull
    private static Book extractBook(Realm realm, Intent intent) {
        int bookId = intent.getIntExtra(BOOK_ID, -1);
        Book book = realm.where(Book.class)
                .equalTo("id", bookId)
                .findFirst();

        if (book == null) throw new NullPointerException();
        else return book;
    }

    private static int extractPageIndex(Intent intent) {
        return intent.getIntExtra(PAGE_INDEX, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Book book = extractBook(realm, intent);
        int pageIndex = extractPageIndex(intent);

        setContentView(R.layout.activity_read);

        BookPageAdapter adapter = new BookPageAdapter(this, book.getPageImages());

        RecyclerView pageRecycler = findViewById(R.id.pageRecycler);
        pageRecycler.setHasFixedSize(true);
        pageRecycler.scrollToPosition(pageIndex);
        pageRecycler.setAdapter(adapter);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(pageRecycler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
