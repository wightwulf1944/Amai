package i.am.shiro.amai.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.DetailThumbnailAdapter;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.service.DownloadService;
import io.realm.Realm;

import static android.content.Intent.ACTION_VIEW;
import static androidx.core.view.ViewCompat.requireViewById;
import static i.am.shiro.amai.util.LayoutUtil.addChild;

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

        requireViewById(view, R.id.button_back)
            .setOnClickListener(v -> onBackClick());

        requireViewById(view, R.id.button_download)
            .setOnClickListener(v -> onDownloadClick());

        requireViewById(view, R.id.button_browser)
            .setOnClickListener(v -> onOpenBrowserClick());

        TextView titleText = requireViewById(view, R.id.titleText);
        titleText.setText(book.getTitle());

        TextView pagesText = requireViewById(view, R.id.text_pages);
        pagesText.setText(getString(R.string.pages_format, book.getPageCount()));

        DetailThumbnailAdapter adapter = new DetailThumbnailAdapter(this, book.getPageThumbnailImages());
        adapter.setOnItemClickListener(this::invokeReadBook);

        RecyclerView previewRecycler = view.findViewById(R.id.previewRecycler);
        previewRecycler.setHasFixedSize(true);
        previewRecycler.setAdapter(adapter);

        LinearLayout tagsLayout = requireViewById(view, R.id.layout_tags);
        addTagGroup(tagsLayout, "Artists", book.getArtistTags());
        addTagGroup(tagsLayout, "Groups", book.getGroupTags());
        addTagGroup(tagsLayout, "Parodies", book.getParodyTags());
        addTagGroup(tagsLayout, "Characters", book.getCharacterTags());
        addTagGroup(tagsLayout, "Language", book.getLanguageTags());
        addTagGroup(tagsLayout, "Categories", book.getCategoryTags());
        addTagGroup(tagsLayout, "Tags", book.getGeneralTags());
    }

    private void addTagGroup(ViewGroup tagsLayout, String label, List<String> tags) {
        if (tags.isEmpty()) return;

        ViewGroup tagGroupLayout = addChild(tagsLayout, R.layout.layout_taggroup);

        TextView labelText = addChild(tagGroupLayout, R.layout.item_label);
        labelText.setText(label);

        for (String tag : tags) {
            TextView tagText = addChild(tagGroupLayout, R.layout.item_tag);
            tagText.setText(tag);
        }
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
