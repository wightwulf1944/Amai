package i.am.shiro.amai.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.BookAdapter;
import i.am.shiro.amai.fragment.dialog.DeleteBookDialogFragment;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.viewmodel.SavedFragmentModel;
import i.am.shiro.amai.widget.SearchInput;

public class SavedFragment extends Fragment {

    public SavedFragment() {
        super(R.layout.fragment_saved);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SearchInput searchInput = view.findViewById(R.id.searchInput);
        searchInput.setOnSubmitListener(s -> {
        });

        BookAdapter adapter = new BookAdapter(this, getLayoutInflater());
        adapter.setOnItemClickListener(this::invokeReadBook);
        adapter.setOnItemLongClickListener(this::invokeDeleteBook);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ViewModelProviders.of(this)
            .get(SavedFragmentModel.class)
            .observeBooks(this, adapter::submitList);
    }

    private void invokeReadBook(Book book) {
        Fragment fragment = ReadFragment.newInstance(book, 0);

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
