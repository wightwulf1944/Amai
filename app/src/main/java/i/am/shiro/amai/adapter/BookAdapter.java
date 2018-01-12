package i.am.shiro.amai.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Book;

/**
 * Created by Shiro on 1/6/2018.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private final Context context;

    private List<Book> data;

    public BookAdapter(Context context) {
        this.context = context;
        data = Collections.emptyList();
    }

    public void setData(List<Book> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
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
        }

        private void bind(Book book) {
            titleText.setText(book.getTitle());
            pageText.setText(book.getPageCountStr());

            String ratioStr = String.format("h,%s:%s",
                    book.getThumbnailWidth(),
                    book.getThumbnailHeight());

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.setDimensionRatio(R.id.thumbnailImage, ratioStr);
            constraintSet.applyTo(constraintLayout);

            Glide.with(context)
                    .load(book.getThumbnailUrl())
                    .into(thumbnailImage);
        }
    }
}
