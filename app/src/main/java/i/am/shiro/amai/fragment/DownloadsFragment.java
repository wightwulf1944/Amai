package i.am.shiro.amai.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.DownloadsAdapter;
import i.am.shiro.amai.model.Content;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

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

        DownloadsAdapter adapter = new DownloadsAdapter();
        adapter.setData(mockData());

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), VERTICAL);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(itemDecoration);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.downloads_action, menu);

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

    private List<Content> mockData() {
        return Arrays.asList(
                new Content("hu"),
                new Content("when I am using TextView with singleLine=\"true\" and ellip"),
                new Content(" I think that TextView should use all available space to display a whole text (not just a part - it shouldn't be cropped) and the textSize"),
                new Content("wer"),
                new Content("wai")
        );
    }
}
