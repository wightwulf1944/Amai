package i.am.shiro.amai.fragment;


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

import java.util.Collections;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.BookAdapter;

import static android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE;
import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadsFragment extends Fragment implements SearchView.OnQueryTextListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloads, container, false);

        BookAdapter adapter = new BookAdapter(this, inflater);
        adapter.setData(Collections.emptyList());

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(getSpanCount(), VERTICAL);
        layoutManager.setGapStrategy(GAP_HANDLING_NONE);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    //TODO
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
}
