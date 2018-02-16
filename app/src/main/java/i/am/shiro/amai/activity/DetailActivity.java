package i.am.shiro.amai.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Book;
import io.realm.Realm;
import timber.log.Timber;

import static android.content.Intent.ACTION_VIEW;
import static com.google.android.flexbox.FlexWrap.WRAP;

/**
 * Created by Shiro on 1/20/2018.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String BOOK_ID = "bookId";

    private final Realm realm = Realm.getDefaultInstance();

    private Book book;

    @NonNull
    public static Intent makeIntent(Context context, int bookId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(BOOK_ID, bookId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBook();

        setContentView(R.layout.activity_detail);

        ImageView coverImage = findViewById(R.id.coverImage);
        Glide.with(this)
                .load(book.getCoverUrl())
                .into(coverImage);

        TextView titleText = findViewById(R.id.titleText);
        titleText.setText(book.getTitle());

        FlexboxLayout tagFlexbox = findViewById(R.id.tagFlexbox);
        tagFlexbox.setFlexWrap(WRAP);
        populateFlexboxWithTags(tagFlexbox, "Parodies:", book.getParodyTags());
        populateFlexboxWithTags(tagFlexbox, "Characters:", book.getCharacterTags());
        populateFlexboxWithTags(tagFlexbox, "Tags:", book.getGeneralTags());
        populateFlexboxWithTags(tagFlexbox, "Artists:", book.getArtistTags());
        populateFlexboxWithTags(tagFlexbox, "Groups:", book.getGroupTags());
        populateFlexboxWithTags(tagFlexbox, "Language:", book.getLanguageTags());
        populateFlexboxWithTags(tagFlexbox, "Categories:", book.getCategoryTags());
    }

    private void initBook() {
        int bookId = getIntent().getIntExtra(BOOK_ID, -1);
        book = realm.where(Book.class)
                .equalTo("id", bookId)
                .findFirst();

        if (book == null) throw new NullPointerException();
    }

    private void populateFlexboxWithTags(FlexboxLayout flexboxLayout, String label, List<String> tags) {
        Timber.w("LOAD TAGS" + label);
        if (tags.isEmpty()) return;

        LayoutInflater inflater = getLayoutInflater();

        TextView labelText = (TextView) inflater.inflate(R.layout.item_label, flexboxLayout, false);
        labelText.setText(label);
        flexboxLayout.addView(labelText);

        for (String generalTag : tags) {
            TextView tagText = (TextView) inflater.inflate(R.layout.item_tag, flexboxLayout, false);
            tagText.setText(generalTag);
            flexboxLayout.addView(tagText);
        }
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
                // TODO
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
}
