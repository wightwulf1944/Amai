package i.am.shiro.amai.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.BookAdapter;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.retrofit.Nhentai;
import timber.log.Timber;

import static android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE;
import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

/**
 * A simple {@link Fragment} subclass.
 */
public class SourceFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_source, container, false);

        BookAdapter adapter = new BookAdapter(getContext());

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, VERTICAL);
        layoutManager.setGapStrategy(GAP_HANDLING_NONE);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        Nhentai.api.getAll(1)
                .flattenAsObservable(bookSearchJson -> bookSearchJson.results)
                .map(Book::new)
                .toList()
                .observeOn(mainThread())
                .subscribe(
                        adapter::setData,
                        throwable -> Timber.d("Failed to get data", throwable)
                );

        return view;
    }
}
