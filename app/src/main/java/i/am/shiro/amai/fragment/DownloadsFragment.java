package i.am.shiro.amai.fragment;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.BookAdapter;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.viewmodel.DownloadsFragmentModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private DownloadsFragmentModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ViewModelProvider viewModelProvider = ViewModelProviders.of(this);
        viewModel = viewModelProvider.get(DownloadsFragmentModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloads, container, false);

        BookAdapter adapter = new BookAdapter(this, inflater);
        adapter.setOnItemClickListener(this::invokeReadBook);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        viewModel.observeBooks(this, adapter::submitList);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.downloads_action, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.filterAction).getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void invokeReadBook(Book book) {
        Bundle args = ReadFragment.makeArgs(book, 0);

        ReadFragment readFragment = new ReadFragment();
        readFragment.setArguments(args);

        requireFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, readFragment)
                .addToBackStack(null)
                .commit();
    }
}
