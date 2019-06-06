package i.am.shiro.amai.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.BookAdapter;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.viewmodel.DownloadsFragmentModel;
import i.am.shiro.amai.widget.SearchInput;

public class DownloadsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloads, container, false);

        SearchInput searchInput = view.findViewById(R.id.searchInput);
        searchInput.setOnSubmitListener(s -> {
        });

        BookAdapter adapter = new BookAdapter(this, inflater);
        adapter.setOnItemClickListener(this::invokeReadBook);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        DownloadsFragmentModel viewModel = ViewModelProviders.of(this)
                .get(DownloadsFragmentModel.class);
        viewModel.observeBooks(this, adapter::submitList);

        return view;
    }

    private void invokeReadBook(Book book) {
        Fragment fragment = ReadFragment.newInstance(book, 0);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }
}
