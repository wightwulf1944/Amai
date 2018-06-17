package i.am.shiro.amai.fragment;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import i.am.shiro.amai.R;
import i.am.shiro.amai.activity.ReadActivity;
import i.am.shiro.amai.adapter.BookAdapter;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.viewmodel.DownloadsFragmentModel;

import static android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE;
import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

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

        // TODO: 6/7/2018 try removing gap handling and observe behavior
        // TODO: 6/7/2018 declare layoutmanager and span count in xml
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(getSpanCount(), VERTICAL);
        layoutManager.setGapStrategy(GAP_HANDLING_NONE);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        viewModel.observeBooks(this, adapter::submitList);

        return view;
    }

    // TODO: 6/7/2018 get variable value from xml
    private int getSpanCount() {
        return 2;
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
        Context context = getContext();
        Intent intent = ReadActivity.makeIntent(context, book, 0);
        startActivity(intent);
    }
}
