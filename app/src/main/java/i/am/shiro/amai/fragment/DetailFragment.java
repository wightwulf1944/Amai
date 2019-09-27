package i.am.shiro.amai.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.DetailAdapter;
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
    static Fragment newInstance(Book book) {
        Bundle args = new Bundle();
        args.putInt(BOOK_ID, book.getId());

        Fragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        super(R.layout.fragment_detail);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackClick());
        toolbar.setOnMenuItemClickListener(this::onActionClick);

        DetailAdapter adapter = new DetailAdapter(this, book);
        adapter.setOnThumbnailClickListener(this::invokeReadBook);

        RecyclerView previewRecycler = view.findViewById(R.id.previewRecycler);
        previewRecycler.setHasFixedSize(true);
        previewRecycler.setAdapter(adapter);

        GridLayoutManager layoutManager = (GridLayoutManager) previewRecycler.getLayoutManager();
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
    }

    private boolean onActionClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_download:
                onDownloadClick();
                break;
            case R.id.action_browser:
                onOpenBrowserClick();
                break;
        }
        return true;
    }

    private void onBackClick() {
        requireFragmentManager().popBackStack();
    }

    private void onDownloadClick() {
        DownloadService.start(requireContext(), book);
    }

    private void onOpenBrowserClick() {
        String webUrl = book.getWebUrl();
        Uri uri = Uri.parse(webUrl);
        Intent intent = new Intent(ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void invokeReadBook(int pageIndex) {
        Fragment fragment = ReadFragment.newInstance(book, pageIndex);

        requireFragmentManager()
            .beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit();
    }
}
