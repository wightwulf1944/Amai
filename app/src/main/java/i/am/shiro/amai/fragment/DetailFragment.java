package i.am.shiro.amai.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.service.DownloadService;
import io.realm.Realm;

import static android.content.Intent.ACTION_VIEW;
import static androidx.core.view.ViewCompat.requireViewById;

public final class DetailFragment extends Fragment {

    private static final String BOOK_ID = "bookId";

    private Realm realm;

    private Book book;

    @NonNull
    public static Fragment newInstance(Book book) {
        Bundle args = new Bundle();
        args.putInt(BOOK_ID, book.getId());

        Fragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();

        int bookId = getArguments().getInt(BOOK_ID, -1);
        book = realm.where(Book.class)
                .equalTo("id", bookId)
                .findFirst();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = requireViewById(view, R.id.toolbar);
        toolbar.inflateMenu(R.menu.detail_action);
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);

        ViewPager viewPager = requireViewById(view, R.id.view_pager);
        viewPager.setAdapter(new Adapter());

        TabLayout tabLayout = requireViewById(view, R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download:
                DownloadService.start(requireContext(), book);
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
        Fragment fragment = ReadFragment.newInstance(book, pageIndex);

        requireFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }

    public Book getBook() {
        return book;
    }

    private class Adapter extends FragmentPagerAdapter {

        private Adapter() {
            super(getChildFragmentManager());
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
                    return new PreviewDetailFragment();
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
