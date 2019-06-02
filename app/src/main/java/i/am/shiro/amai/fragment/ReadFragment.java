package i.am.shiro.amai.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.BookPageAdapter;
import i.am.shiro.amai.model.Book;
import io.realm.Realm;

import static android.support.v4.view.ViewCompat.requireViewById;

public class ReadFragment extends Fragment {

    private static final String BOOK_ID = "bookId";

    private static final String PAGE_INDEX = "pageIndex";

    private Realm realm;

    @NonNull
    public static Bundle makeArgs(Book book, int pageIndex) {
        Bundle intent = new Bundle();
        intent.putInt(BOOK_ID, book.getId());
        intent.putInt(PAGE_INDEX, pageIndex);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_read, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Book book = extractBook();
        int pageIndex = extractPageIndex();

        BookPageAdapter adapter = new BookPageAdapter(this, book.getPageImages());

        RecyclerView pageRecycler = requireViewById(view, R.id.pageRecycler);
        pageRecycler.setHasFixedSize(true);
        pageRecycler.scrollToPosition(pageIndex);
        pageRecycler.setAdapter(adapter);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(pageRecycler);
    }

    private Book extractBook() {
        int bookId = getArguments().getInt(BOOK_ID, -1);
        Book book = realm.where(Book.class)
                .equalTo("id", bookId)
                .findFirst();

        return Objects.requireNonNull(book);
    }

    private int extractPageIndex() {
        return getArguments().getInt(PAGE_INDEX, 0);
    }
}
