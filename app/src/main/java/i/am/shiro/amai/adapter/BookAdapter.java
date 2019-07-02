package i.am.shiro.amai.adapter;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.IntConsumer;
import com.bumptech.glide.Glide;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.Image;

import static java.lang.String.valueOf;

public class BookAdapter extends ListAdapter<Book, BookAdapter.ViewHolder> {

    private final Fragment parentFragment;

    private final LayoutInflater inflater;

    private Consumer<Book> onItemClickListener;

    private IntConsumer onPositionBindListener;

    public BookAdapter(Fragment parentFragment, LayoutInflater inflater) {
        super(new DiffCallback());
        this.parentFragment = parentFragment;
        this.inflater = inflater;
        setHasStableIds(true);
    }

    public void setOnItemClickListener(Consumer<Book> listener) {
        onItemClickListener = listener;
    }

    public void setOnPositionBindListener(IntConsumer listener) {
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
        if (onPositionBindListener != null) onPositionBindListener.accept(position);
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
            pageText.setText(valueOf(book.getPageCount()));

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

    private static class DiffCallback extends DiffUtil.ItemCallback<Book> {

        @Override
        public boolean areItemsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
            return oldItem.isDownloaded() == newItem.isDownloaded();
        }
    }
}
