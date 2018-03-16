package i.am.shiro.amai.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.PreviewThumbnailAdapter;
import i.am.shiro.amai.adapter.TagAdapter;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.service.DownloadService;
import io.realm.Realm;

import static android.content.Intent.ACTION_VIEW;

/**
 * Created by Shiro on 1/20/2018.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String BOOK_ID = "bookId";

    private final Realm realm = Realm.getDefaultInstance();

    private Book book;

    @NonNull
    public static Intent makeIntent(Context context, Book book) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(BOOK_ID, book.getId());
        return intent;
    }

    private static Book extractBook(Realm realm, Intent intent) {
        int bookId = intent.getIntExtra(BOOK_ID, -1);
        Book book = realm.where(Book.class)
                .equalTo("id", bookId)
                .findFirst();

        if (book == null) throw new NullPointerException();
        else return book;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        book = extractBook(realm, getIntent());

        setContentView(R.layout.activity_detail);

        ConstraintLayout coverImageConstraintLayout = findViewById(R.id.coverImageConstraintLayout);

        String ratioStr = String.format("h,%s:%s",
                book.getCoverImage().getWidth(),
                book.getCoverImage().getHeight());

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(coverImageConstraintLayout);
        constraintSet.setDimensionRatio(R.id.coverImage, ratioStr);
        constraintSet.applyTo(coverImageConstraintLayout);

        ImageView coverImage = findViewById(R.id.coverImage);
        Glide.with(this)
                .load(book.getCoverImage().getUrl())
                .into(coverImage);

        PreviewThumbnailAdapter adapter = new PreviewThumbnailAdapter(this, book.getPageThumbnailImages());
        adapter.setOnItemClickListener(this::invokeReadBook);

        RecyclerView previewRecycler = findViewById(R.id.previewRecycler);
        previewRecycler.setHasFixedSize(true);
        previewRecycler.setAdapter(adapter);

        TextView titleText = findViewById(R.id.titleText);
        titleText.setText(book.getTitle());

        TagAdapter tagAdapter = new TagAdapter(book);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexWrap(FlexWrap.WRAP);

        RecyclerView tagRecycler = findViewById(R.id.tagRecycler);
        tagRecycler.setAdapter(tagAdapter);
        tagRecycler.setLayoutManager(layoutManager);
        tagRecycler.setHasFixedSize(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download:
                DownloadService.start(this, book);
                return true;
            case R.id.open_in_browser:
                invokeOpenInBrowser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void invokeOpenInBrowser() {
        String webUrl = book.getWebUrl();
        Uri uri = Uri.parse(webUrl);
        Intent intent = new Intent(ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void invokeReadBook(int pageIndex) {
        Intent intent = ReadActivity.makeIntent(this, book, pageIndex);
        startActivity(intent);
    }
}
