package i.am.shiro.amai.adapter;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.function.Consumer;
import com.bumptech.glide.Glide;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.Image;

/**
 * Created by Shiro on 1/6/2018.
 */

public class BookAdapter extends ListAdapter<Book, BookAdapter.ViewHolder> {

    private final Fragment parentFragment;

    private final LayoutInflater inflater;

    private Consumer<Book> onItemClickListener;

    private Consumer<Integer> onPositionBindListener;

    public BookAdapter(Fragment parentFragment, LayoutInflater inflater) {
        super(new DiffCalback());
        this.parentFragment = parentFragment;
        this.inflater = inflater;
        setHasStableIds(true);
    }

    public void setOnItemClickListener(Consumer<Book> listener) {
        onItemClickListener = listener;
    }

    public void setOnPositionBindListener(Consumer<Integer> listener) {
        onPositionBindListener = listener;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_staggered_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = getItem(position);
        holder.bind(book);
        onPositionBindListener.accept(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout constraintLayout;

        private final ImageView thumbnailImage;

        private final TextView titleText;

        private final TextView pageText;

        private Book book;

        ViewHolder(View view) {
            super(view);
            constraintLayout = view.findViewById(R.id.constraintLayout);
            thumbnailImage = view.findViewById(R.id.thumbnailImage);
            titleText = view.findViewById(R.id.titleText);
            pageText = view.findViewById(R.id.pageText);
            view.setOnClickListener(v -> onItemClick());
        }

        private void onItemClick() {
            onItemClickListener.accept(book);
        }

        private void bind(Book book) {
            this.book = book;

            titleText.setText(book.getTitle());
            pageText.setText(book.getPageCountStr());

            Image coverThumbnailImage = book.getCoverThumbnailImage();

            String ratioStr = String.format("h,%s:%s",
                    coverThumbnailImage.getWidth(),
                    coverThumbnailImage.getHeight());

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.setDimensionRatio(R.id.thumbnailImage, ratioStr);
            constraintSet.applyTo(constraintLayout);

            Glide.with(parentFragment)
                    .load(coverThumbnailImage.getUrl())
                    .into(thumbnailImage);
        }
    }

    private static class DiffCalback extends DiffUtil.ItemCallback<Book> {

        @Override
        public boolean areItemsTheSame(Book oldItem, Book newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Book oldItem, Book newItem) {
            return oldItem.isDownloaded() == newItem.isDownloaded();
        }
    }
}
