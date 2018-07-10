package i.am.shiro.amai.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import i.am.shiro.amai.R;
import i.am.shiro.amai.fragment.AboutIntroFragment;
import i.am.shiro.amai.fragment.InfoDetailFragment;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.service.DownloadService;
import io.realm.Realm;

import static android.content.Intent.ACTION_VIEW;

public class DetailActivity extends AppCompatActivity {

    private static final String BOOK_ID = "bookId";

    @NonNull
    public static Intent makeIntent(Context context, Book book) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(BOOK_ID, book.getId());
        return intent;
    }

    private final Realm realm = Realm.getDefaultInstance();

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int bookId = getIntent().getIntExtra(BOOK_ID, -1);
        book = realm.where(Book.class)
                .equalTo("id", bookId)
                .findFirst();

        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.detail_action);
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new Adapter());

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private boolean onMenuItemClick(MenuItem item) {
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

    public void invokeReadBook(int pageIndex) {
        Intent intent = ReadActivity.makeIntent(this, book, pageIndex);
        startActivity(intent);
    }

    public Book getBook() {
        return book;
    }

    private class Adapter extends FragmentPagerAdapter {

        private Adapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new InfoDetailFragment();
                case 1:
                    return new AboutIntroFragment();
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Info";
                case 1:
                    return "Preview";
                default:
                    return null;
            }
        }
    }
}
