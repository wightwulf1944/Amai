package i.am.shiro.amai.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import i.am.shiro.amai.R
import i.am.shiro.amai.constant.DownloadStatus
import i.am.shiro.amai.model.DownloadJob

class DownloadJobAdapter(
        private val onDismiss: (DownloadJob) -> Unit
) : ListAdapter<DownloadJob, DownloadJobAdapter.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == DownloadStatus.DONE) {
            val view = inflater.inflate(R.layout.item_download_done, parent, false)
            DoneViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_download_generic, parent, false)
            GenericViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = getItem(position)
        holder.bindData(job)
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleText: TextView = itemView.findViewById(R.id.titleText)

        @CallSuper
        internal open fun bindData(job: DownloadJob) {
            titleText.text = job.title
        }
    }

    internal class GenericViewHolder(itemView: View) : ViewHolder(itemView) {

        private val statusText: TextView = itemView.findViewById(R.id.statusText)

        override fun bindData(job: DownloadJob) {
            super.bindData(job)
            statusText.text = job.statusString
        }
    }

    internal inner class DoneViewHolder(itemView: View) : ViewHolder(itemView) {

        private val dismissButton: MaterialButton = itemView.findViewById(R.id.dismissButton)

        override fun bindData(job: DownloadJob) {
            super.bindData(job)
            dismissButton.setOnClickListener { onDismiss(job) }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<DownloadJob>() {

        override fun areItemsTheSame(oldItem: DownloadJob, newItem: DownloadJob): Boolean {
            return oldItem.bookId == newItem.bookId
        }

        override fun areContentsTheSame(oldItem: DownloadJob, newItem: DownloadJob): Boolean {
            return oldItem.status == newItem.status
        }
    }
}
