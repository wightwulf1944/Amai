package i.am.shiro.amai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.annimon.stream.function.Consumer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;

import java.util.List;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Image;

// TODO optimize by specifying item dimensions on bind
public class DetailThumbnailAdapter extends Adapter<DetailThumbnailAdapter.ViewHolder> {

    private final Fragment parentFragment;

    private final List<Image> pageThumbnailImages;

    private final LayoutInflater inflater;

    private Consumer<Integer> onItemClickListener;

    public DetailThumbnailAdapter(Fragment parentFragment, List<Image> pageThumbnailImages) {
        this.parentFragment = parentFragment;
        this.pageThumbnailImages = pageThumbnailImages;
        inflater = parentFragment.getLayoutInflater();
    }

    public void setOnItemClickListener(Consumer<Integer> listener) {
        onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_preview_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image pageThumbnailImage = pageThumbnailImages.get(position);
        holder.bind(pageThumbnailImage);
    }

    @Override
    public int getItemCount() {
        return pageThumbnailImages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ConstraintLayout constraintLayout;

        final ImageView previewImage;

        ViewHolder(View itemView) {
            super(itemView);
            constraintLayout = (ConstraintLayout) itemView;
            previewImage = itemView.findViewById(R.id.thumbnailImage);
            itemView.setOnClickListener(v -> onItemClick());
        }

        private void onItemClick() {
            onItemClickListener.accept(getAdapterPosition());
        }

        void bind(Image image) {
            String ratioStr = String.format("v,%s:%s",
                    image.getWidth(),
                    image.getHeight());

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.setDimensionRatio(R.id.thumbnailImage, ratioStr);
            constraintSet.applyTo(constraintLayout);

            Glide.with(parentFragment)
                .load(image.getUrl())
                .format(DecodeFormat.PREFER_RGB_565)
                .into(previewImage);
        }
    }
}
