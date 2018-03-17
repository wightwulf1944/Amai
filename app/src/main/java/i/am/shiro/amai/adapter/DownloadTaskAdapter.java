package i.am.shiro.amai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.DownloadJob;
import io.realm.RealmResults;

/**
 * Created by Shiro on 3/10/2018.
 */

public class DownloadTaskAdapter extends RecyclerView.Adapter<DownloadTaskAdapter.ViewHolder> {

    private RealmResults<DownloadJob> downloadJobs;

    public DownloadTaskAdapter(RealmResults<DownloadJob> downloadJobs) {
        this.downloadJobs = downloadJobs;
        downloadJobs.addChangeListener(downloadTasks1 -> notifyDataSetChanged());
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
        DownloadJob job = downloadJobs.get(position);
        holder.bindData(job);
    }

    @Override
    public int getItemCount() {
        return downloadJobs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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
}
