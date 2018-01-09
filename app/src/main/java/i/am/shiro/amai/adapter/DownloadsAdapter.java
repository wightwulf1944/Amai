package i.am.shiro.amai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Content;

/**
 * Created by Shiro on 1/6/2018.
 */

public class DownloadsAdapter extends RecyclerView.Adapter<DownloadsAdapter.ViewHolder> {

    private List<Content> data;

    public void setData(List<Content> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_download, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Content content = data.get(position);
        holder.bind(content);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView thumbnailImage;

        private final TextView titleText;

        private final TextView pageText;

        ViewHolder(View view) {
            super(view);
            thumbnailImage = view.findViewById(R.id.thumbnailImage);
            titleText = view.findViewById(R.id.titleText);
            pageText = view.findViewById(R.id.pageText);
        }

        private void bind(Content content) {
            titleText.setText(content.getTitle());
            pageText.setText(content.getPageCountStr());
        }
    }
}
