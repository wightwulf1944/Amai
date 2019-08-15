package i.am.shiro.amai.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.BookPageAdapter;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.Image;
import i.am.shiro.amai.widget.PageRecyclerView;
import io.realm.Realm;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static androidx.core.view.ViewCompat.requireViewById;

public class ReadFragment extends Fragment {

    private static final String BOOK_ID = "bookId";

    private static final String PAGE_INDEX = "pageIndex";

    private Realm realm;

    public ReadFragment() {
        super(R.layout.fragment_read);
    }

    @NonNull
    static Fragment newInstance(Book book, int pageIndex) {
        Bundle args = new Bundle();
        args.putInt(BOOK_ID, book.getId());
        args.putInt(PAGE_INDEX, pageIndex);

        Fragment fragment = new ReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        realm = Realm.getDefaultInstance();

        requireActivity()
            .getWindow()
            .addFlags(FLAG_FULLSCREEN);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        realm.close();

        requireActivity()
            .getWindow()
            .clearFlags(FLAG_FULLSCREEN);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        BookPageAdapter adapter = new BookPageAdapter(this, extractImages());

        PageRecyclerView pageRecycler = requireViewById(view, R.id.pageRecycler);
        pageRecycler.setHasFixedSize(true);
        pageRecycler.setAdapter(adapter);
        pageRecycler.requestFocus();

        if (savedInstanceState == null) {
            pageRecycler.scrollToPosition(extractPageIndex());
        }
    }

    private List<Image> extractImages() {
        int bookId = getArguments().getInt(BOOK_ID, -1);
        return realm.where(Book.class)
            .equalTo("id", bookId)
            .findFirst()
            .getPageImages();
    }

    private int extractPageIndex() {
        return getArguments()
            .getInt(PAGE_INDEX, 0);
    }
}
