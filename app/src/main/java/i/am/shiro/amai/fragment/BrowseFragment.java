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
import i.am.shiro.amai.viewmodel.BrowseFragmentModel;
import i.am.shiro.amai.widget.SearchInput;

public class BrowseFragment extends Fragment {

    private BrowseFragmentModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this)
                .get(BrowseFragmentModel.class);

        if (savedInstanceState == null) {
            viewModel.init();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        SearchInput searchInput = view.findViewById(R.id.searchInput);
        searchInput.setOnSubmitListener(viewModel::search);

        BookAdapter adapter = new BookAdapter(this, inflater);
        adapter.setOnItemClickListener(this::invokeViewDetails);
        adapter.setOnPositionBindListener(viewModel::onPositionBind);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        viewModel.observeBooks(this, adapter::submitList);

        return view;
    }

    private void invokeViewDetails(Book book) {
        Fragment fragment = DetailFragment.newInstance(book);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }
}
