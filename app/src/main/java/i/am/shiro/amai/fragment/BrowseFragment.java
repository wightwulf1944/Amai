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
import i.am.shiro.amai.activity.DetailActivity;
import i.am.shiro.amai.adapter.BookAdapter;
import i.am.shiro.amai.viewmodel.BrowseFragmentViewModel;

import static android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE;
import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends Fragment implements SearchView.OnQueryTextListener {

    private BrowseFragmentViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ViewModelProvider viewModelProvider = ViewModelProviders.of(this);
        viewModel = viewModelProvider.get(BrowseFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_source, container, false);

        BookAdapter adapter = new BookAdapter(this, inflater);
        adapter.setOnItemClickListener(this::onItemClicked);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(getSpanCount(), VERTICAL);
        layoutManager.setGapStrategy(GAP_HANDLING_NONE);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        viewModel.observeBooks(this, adapter::setData);
        viewModel.fetchBooks();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.browse_action, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.searchAction).getActionView();
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

    //TODO
    private int getSpanCount() {
        return 2;
    }

    private void onItemClicked(int position) {
        Context context = getContext();
        int bookId = viewModel.getBooks().get(position).getId();
        Intent intent = DetailActivity.makeIntent(context, bookId);
        startActivity(intent);
    }
}
