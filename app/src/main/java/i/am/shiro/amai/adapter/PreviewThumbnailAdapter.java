package i.am.shiro.amai.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Image;

/**
 * Created by Shiro on 2/19/2018.
 * TODO optimize by specifying item dimensions on bind
 */

public class PreviewThumbnailAdapter extends Adapter<PreviewThumbnailAdapter.ViewHolder> {

    private final Activity parentActivity;

    private final List<Image> pageThumbnailImages;

    private final LayoutInflater inflater;

    public PreviewThumbnailAdapter(Activity parentActivity, List<Image> pageThumbnailImages) {
        this.parentActivity = parentActivity;
        this.pageThumbnailImages = pageThumbnailImages;
        inflater = LayoutInflater.from(parentActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_preview_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image pageThumbnailImage = pageThumbnailImages.get(position);
        holder.bind(pageThumbnailImage);
    }

    @Override
    public int getItemCount() {
        return pageThumbnailImages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView previewImage;

        ViewHolder(View itemView) {
            super(itemView);
            previewImage = (ImageView) itemView;
        }

        void bind(Image image) {
            Glide.with(parentActivity)
                    .load(image.getUrl())
                    .into(previewImage);
        }
    }
}
