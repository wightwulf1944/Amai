package i.am.shiro.amai.fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.BookAdapter;
import i.am.shiro.amai.fragment.dialog.DeleteBookDialogFragment;
import i.am.shiro.amai.fragment.dialog.PlaceholderDialogFragment;
import i.am.shiro.amai.fragment.dialog.SavedSortOrderDialogFragment;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.viewmodel.SavedFragmentModel;
import i.am.shiro.amai.widget.SearchInput;

public class SavedFragment extends Fragment {

    public SavedFragment() {
        super(R.layout.fragment_saved);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(this::onActionClick);

        SearchInput searchInput = view.findViewById(R.id.searchInput);
        searchInput.setOnSubmitListener(s -> invokeSearch());

        BookAdapter adapter = new BookAdapter(this, getLayoutInflater());
        adapter.setOnItemClickListener(this::invokeViewDetails);
        adapter.setOnItemLongClickListener(this::invokeDeleteBook);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ViewModelProviders.of(this)
            .get(SavedFragmentModel.class)
            .observeBooks(this, adapter::submitList);
    }

    private boolean onActionClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_sort:
                invokeSort();
                break;
            case R.id.action_help:
                invokeHelp();
                break;
        }
        return true;
    }

    private void invokeSearch() {
        PlaceholderDialogFragment dialogFragment = new PlaceholderDialogFragment();
        dialogFragment.show(getChildFragmentManager(), null);
    }

    private void invokeSort() {
        SavedSortOrderDialogFragment dialogFragment = new SavedSortOrderDialogFragment();
        dialogFragment.show(getChildFragmentManager(), null);
    }

    private void invokeHelp() {
        PlaceholderDialogFragment dialogFragment = new PlaceholderDialogFragment();
        dialogFragment.show(getChildFragmentManager(), null);
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

    private void invokeDeleteBook(Book book) {
        DeleteBookDialogFragment dialogFragment = new DeleteBookDialogFragment();
        dialogFragment.setArguments(book);
        dialogFragment.show(getChildFragmentManager(), null);
    }
}
