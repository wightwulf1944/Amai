package i.am.shiro.amai.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.function.Consumer;
import com.bumptech.glide.Glide;

import java.util.List;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Book;

/**
 * Created by Shiro on 1/6/2018.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private final Fragment parentFragment;

    private final LayoutInflater inflater;

    private List<Book> data;

    private Consumer<Integer> onItemClickListener;

    public BookAdapter(Fragment parentFragment, LayoutInflater inflater) {
        this.parentFragment = parentFragment;
        this.inflater = inflater;
        setHasStableIds(true);
    }

    public void setData(List<Book> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(Consumer<Integer> listener) {
        onItemClickListener = listener;
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_staggered_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = data.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout constraintLayout;

        private final ImageView thumbnailImage;

        private final TextView titleText;

        private final TextView pageText;

        ViewHolder(View view) {
            super(view);
            constraintLayout = view.findViewById(R.id.constraintLayout);
            thumbnailImage = view.findViewById(R.id.thumbnailImage);
            titleText = view.findViewById(R.id.titleText);
            pageText = view.findViewById(R.id.pageText);
            view.setOnClickListener(v -> onItemClicked());
        }

        private void onItemClicked() {
            onItemClickListener.accept(getAdapterPosition());
        }

        private void bind(Book book) {
            titleText.setText(book.getTitle());
            pageText.setText(book.getPageCountStr());

            String ratioStr = String.format("h,%s:%s",
                    book.getPreviewWidth(),
                    book.getPreviewHeight());

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.setDimensionRatio(R.id.thumbnailImage, ratioStr);
            constraintSet.applyTo(constraintLayout);

            Glide.with(parentFragment)
                    .load(book.getPreviewUrl())
                    .into(thumbnailImage);
        }
    }
}
