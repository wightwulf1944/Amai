package i.am.shiro.amai.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.BookAdapter;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.viewmodel.BrowseFragmentModel;
import i.am.shiro.amai.widget.SearchInput;

public class BrowseFragment extends Fragment {

    private BrowseFragmentModel viewModel;

    public BrowseFragment() {
        super(R.layout.fragment_browse);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this)
                .get(BrowseFragmentModel.class);

        if (savedInstanceState == null) {
            viewModel.onNewInstanceCreated();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SearchInput searchInput = view.findViewById(R.id.searchInput);
        searchInput.setOnSubmitListener(viewModel::search);

        BookAdapter adapter = new BookAdapter(this, getLayoutInflater());
        adapter.setOnItemClickListener(this::invokeViewDetails);
        adapter.setOnPositionBindListener(viewModel::onPositionBind);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ProgressBar loadingProgress = view.findViewById(R.id.progress);

        viewModel.observeBooks(this, adapter::submitList);
        viewModel.observeLoadingState(this, isLoading ->
            loadingProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE));
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
