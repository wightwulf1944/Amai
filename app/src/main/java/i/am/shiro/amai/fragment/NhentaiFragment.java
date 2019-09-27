package i.am.shiro.amai.fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.BookAdapter;
import i.am.shiro.amai.fragment.dialog.NhentaiSortOrderDialogFragment;
import i.am.shiro.amai.fragment.dialog.SearchConstantsDialogFragment;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.viewmodel.NhentaiFragmentModel;
import i.am.shiro.amai.widget.SearchInput;

public class NhentaiFragment extends Fragment {

    private NhentaiFragmentModel viewModel;

    public NhentaiFragment() {
        super(R.layout.fragment_nhentai);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this)
            .get(NhentaiFragmentModel.class);

        if (savedInstanceState == null) {
            viewModel.onNewInstanceCreated();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(this::onActionClick);

        SearchInput searchInput = toolbar.findViewById(R.id.searchInput);
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

    private boolean onActionClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_sort:
                invokeSort();
                break;
            case R.id.action_constants:
                invokeConstants();
                break;
        }
        return true;
    }


    private void invokeSort() {
        NhentaiSortOrderDialogFragment dialogFragment = new NhentaiSortOrderDialogFragment();
        dialogFragment.show(getChildFragmentManager(), null);
    }

    private void invokeConstants() {
        SearchConstantsDialogFragment fragment = new SearchConstantsDialogFragment();
        fragment.show(requireFragmentManager(), null);
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
