package i.am.shiro.amai.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.DetailThumbnailAdapter;
import i.am.shiro.amai.adapter.TagAdapter;
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
        Toolbar toolbar = requireViewById(view, R.id.toolbar);
        toolbar.inflateMenu(R.menu.detail_action);
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);

        TextView titleText = view.findViewById(R.id.titleText);
        titleText.setText(book.getTitle());

        ConstraintLayout coverImageConstraintLayout = view.findViewById(R.id.constraint_layout);

        String ratioStr = String.format("%s:%s",
            book.getCoverImage()
                .getWidth(),
            book.getCoverImage()
                .getHeight());

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(coverImageConstraintLayout);
        constraintSet.setDimensionRatio(R.id.coverImage, ratioStr);
        constraintSet.applyTo(coverImageConstraintLayout);

        ImageView coverImage = view.findViewById(R.id.coverImage);

        String coverImageUrl = book.getCoverImage()
            .getUrl();

        Glide.with(this)
            .load(coverImageUrl)
            .into(coverImage);

        DetailThumbnailAdapter adapter = new DetailThumbnailAdapter(this, book.getPageThumbnailImages());
        adapter.setOnItemClickListener(this::invokeReadBook);

        RecyclerView previewRecycler = view.findViewById(R.id.previewRecycler);
        previewRecycler.setHasFixedSize(true);
        previewRecycler.setAdapter(adapter);

        TagAdapter tagAdapter = new TagAdapter(book);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireContext());
        layoutManager.setFlexWrap(FlexWrap.WRAP);

        RecyclerView tagRecycler = view.findViewById(R.id.tagRecycler);
        tagRecycler.setAdapter(tagAdapter);
        tagRecycler.setLayoutManager(layoutManager);
        tagRecycler.setHasFixedSize(true);
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
}
