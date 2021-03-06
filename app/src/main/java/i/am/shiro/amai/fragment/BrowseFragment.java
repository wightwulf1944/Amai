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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import i.am.shiro.amai.R;
import i.am.shiro.amai.activity.DetailActivity;
import i.am.shiro.amai.adapter.BookAdapter;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.viewmodel.BrowseFragmentModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends Fragment implements SearchView.OnQueryTextListener {

    private BrowseFragmentModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        viewModel = ViewModelProviders.of(this).get(BrowseFragmentModel.class);

        if (savedInstanceState == null) {
            viewModel.init();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        BookAdapter adapter = new BookAdapter(this, inflater);
        adapter.setOnItemClickListener(this::invokeViewDetails);
        adapter.setOnPositionBindListener(viewModel::onPositionBind);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        viewModel.observeBooks(this, adapter::submitList);

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
        viewModel.search(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void invokeViewDetails(Book book) {
        Context context = getContext();
        Intent intent = DetailActivity.makeIntent(context, book);
        startActivity(intent);
    }
}
