package i.am.shiro.amai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Image;

public final class BookPageAdapter extends RecyclerView.Adapter<BookPageAdapter.ViewHolder> {

    private final Fragment parentFragment;

    private final LayoutInflater inflater;

    private final List<Image> data;

    public BookPageAdapter(Fragment parentFragment, List<Image> data) {
        this.parentFragment = parentFragment;
        inflater = parentFragment.getLayoutInflater();
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_read_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = data.get(position)
            .getUrl();
        holder.bind(position, url);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        Glide.with(parentFragment)
            .clear(holder.pageImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView pageText;
        private final ImageView pageImage;

        ViewHolder(View itemView) {
            super(itemView);
            pageText = itemView.findViewById(R.id.pageText);
            pageImage = itemView.findViewById(R.id.pageImage);
        }

        void bind(int position, String url) {
            String pageStr = String.valueOf(position + 1);
            pageText.setText(pageStr);

            Glide.with(parentFragment)
                .load(url)
                .thumbnail(0.1f)
                .into(pageImage);
        }
    }
}
