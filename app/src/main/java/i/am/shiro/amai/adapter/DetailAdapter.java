package i.am.shiro.amai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.annimon.stream.function.Consumer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;

import java.util.List;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.Image;

import static androidx.core.view.ViewCompat.requireViewById;
import static i.am.shiro.amai.util.LayoutUtil.addChild;

public class DetailAdapter extends Adapter<ViewHolder> {

    private final Fragment parentFragment;

    private final Book book;

    private final List<Image> pageThumbnailImages;

    private Consumer<Integer> onThumbnailClickListener;

    public DetailAdapter(Fragment parentFragment, Book book) {
        this.parentFragment = parentFragment;
        this.book = book;
        this.pageThumbnailImages = book.getPageThumbnailImages();
    }

    public void setOnThumbnailClickListener(Consumer<Integer> listener) {
        onThumbnailClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return pageThumbnailImages.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == 0) {
            View view = inflater.inflate(R.layout.item_detail_header, parent, false);
            return new HeaderViewHolder(view, book);
        } else if (viewType == 1) {
            View view = inflater.inflate(R.layout.item_preview_image, parent, false);
            return new ThumbnailViewHolder(view);
        }
        throw new RuntimeException();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder instanceof ThumbnailViewHolder) {
            Image pageThumbnailImage = pageThumbnailImages.get(position - 1);
            ThumbnailViewHolder thumbnailHolder = (ThumbnailViewHolder) holder;
            thumbnailHolder.bind(pageThumbnailImage);
        }
    }

    private static class HeaderViewHolder extends ViewHolder {

        private final LinearLayout tagsLayout;

        private HeaderViewHolder(@NonNull View itemView, Book book) {
            super(itemView);

            TextView titleText = requireViewById(itemView, R.id.titleText);
            titleText.setText(book.getTitle());

            String pageCount = this.itemView.getResources()
                .getString(R.string.pages_format, book.getPageCount());
            TextView pagesText = requireViewById(itemView, R.id.text_pages);
            pagesText.setText(pageCount);

            tagsLayout = requireViewById(itemView, R.id.layout_tags);
            addTagGroup("Artists", book.getArtistTags());
            addTagGroup("Groups", book.getGroupTags());
            addTagGroup("Parodies", book.getParodyTags());
            addTagGroup("Characters", book.getCharacterTags());
            addTagGroup("Language", book.getLanguageTags());
            addTagGroup("Categories", book.getCategoryTags());
            addTagGroup("Tags", book.getGeneralTags());
        }

        private void addTagGroup(String label, List<String> tags) {
            if (tags.isEmpty()) return;

            ViewGroup tagGroupLayout = addChild(tagsLayout, R.layout.layout_taggroup);

            TextView labelText = addChild(tagGroupLayout, R.layout.item_label);
            labelText.setText(label);

            for (String tag : tags) {
                TextView tagText = addChild(tagGroupLayout, R.layout.item_tag);
                tagText.setText(tag);
            }
        }
    }

    private class ThumbnailViewHolder extends ViewHolder {

        private final ImageView previewImage;

        private ThumbnailViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> onItemClick());
            previewImage = itemView.findViewById(R.id.thumbnailImage);
        }

        private void onItemClick() {
            onThumbnailClickListener.accept(getAdapterPosition() - 1);
        }

        private void bind(Image image) {
            int width = image.getWidth();
            int maxHeight = (int) ((width / 200d) * 364d);
            int height = Math.min(image.getHeight(), maxHeight);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) previewImage.getLayoutParams();
            layoutParams.dimensionRatio = String.format("%s:%s", width, height);

            Glide.with(parentFragment)
                .load(image.getUrl())
                .format(DecodeFormat.PREFER_RGB_565)
                .into(previewImage);
        }
    }
}
