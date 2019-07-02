package i.am.shiro.amai.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.DetailThumbnailAdapter;
import i.am.shiro.amai.adapter.TagAdapter;
import i.am.shiro.amai.model.Book;

public class InfoDetailFragment extends Fragment {

    private DetailFragment parentFragment;

    private Book book;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentFragment = (DetailFragment) getParentFragment();
        book = parentFragment.getBook();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infodetail, container, false);

        TextView titleText = view.findViewById(R.id.titleText);
        titleText.setText(book.getTitle());

        ConstraintLayout coverImageConstraintLayout = view.findViewById(R.id.constraint_layout);

        String ratioStr = String.format("%s:%s",
                book.getCoverImage().getWidth(),
                book.getCoverImage().getHeight());

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(coverImageConstraintLayout);
        constraintSet.setDimensionRatio(R.id.coverImage, ratioStr);
        constraintSet.applyTo(coverImageConstraintLayout);

        ImageView coverImage = view.findViewById(R.id.coverImage);
        Glide.with(this)
                .load(book.getCoverImage().getUrl())
                .into(coverImage);

        DetailThumbnailAdapter adapter = new DetailThumbnailAdapter(this, book.getPageThumbnailImages());
        adapter.setOnItemClickListener(parentFragment::invokeReadBook);

        RecyclerView previewRecycler = view.findViewById(R.id.previewRecycler);
        previewRecycler.setHasFixedSize(true);
        previewRecycler.setAdapter(adapter);

        TagAdapter tagAdapter = new TagAdapter(book);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireContext());
        layoutManager.setFlexWrap(FlexWrap.WRAP);

        RecyclerView tagRecycler = view.findViewById(R.id.tagRecycler);
        tagRecycler.setAdapter(tagAdapter);
        tagRecycler.setLayoutManager(layoutManager);
        tagRecycler.setHasFixedSize(true);

        return view;
    }
}
