package i.am.shiro.amai.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.BookPageAdapter;
import i.am.shiro.amai.model.Book;
import io.realm.Realm;

import java.util.Objects;

import static androidx.core.view.ViewCompat.requireViewById;

public class ReadFragment extends Fragment {

    private static final String BOOK_ID = "bookId";

    private static final String PAGE_INDEX = "pageIndex";

    private Realm realm;

    @NonNull
    public static Fragment newInstance(Book book, int pageIndex) {
        Bundle args = new Bundle();
        args.putInt(BOOK_ID, book.getId());
        args.putInt(PAGE_INDEX, pageIndex);

        Fragment fragment = new ReadFragment();
        fragment.setArguments(args);
        return fragment;
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
