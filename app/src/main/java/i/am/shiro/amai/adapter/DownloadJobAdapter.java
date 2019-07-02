package i.am.shiro.amai.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import i.am.shiro.amai.R;
import i.am.shiro.amai.model.DownloadJob;

public class DownloadJobAdapter extends ListAdapter<DownloadJob, DownloadJobAdapter.ViewHolder> {

    public DownloadJobAdapter() {
        super(new DiffCalback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_queue_generic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DownloadJob job = getItem(position);
        holder.bindData(job);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleText;

        private final TextView statusText;

        ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            statusText = itemView.findViewById(R.id.statusText);
        }

        void bindData(DownloadJob job) {
            titleText.setText(job.getTitle());
            statusText.setText(job.getStatusString());
        }
    }

    private static class DiffCalback extends DiffUtil.ItemCallback<DownloadJob> {

        @Override
        public boolean areItemsTheSame(@NonNull DownloadJob oldItem, @NonNull DownloadJob newItem) {
            return oldItem.getBookId() == newItem.getBookId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull DownloadJob oldItem, @NonNull DownloadJob newItem) {
            return oldItem.getStatus() == newItem.getStatus();
        }
    }
}
