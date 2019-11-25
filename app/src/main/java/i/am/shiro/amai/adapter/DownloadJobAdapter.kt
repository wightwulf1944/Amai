package i.am.shiro.amai.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import i.am.shiro.amai.R
import i.am.shiro.amai.constant.DownloadStatus
import i.am.shiro.amai.model.DownloadJob
import i.am.shiro.amai.util.inflateChild

class DownloadJobAdapter(
        private val onDismiss: (DownloadJob) -> Unit,
        private val onCancel: (DownloadJob) -> Unit,
        private val onRetry: (DownloadJob) -> Unit,
        private val onPause: (DownloadJob) -> Unit
) : ListAdapter<DownloadJob, DownloadJobAdapter.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            DownloadStatus.DONE -> {
                DoneViewHolder(parent.inflateChild(R.layout.item_download_done))
            }
            DownloadStatus.FAILED -> {
                FailedViewHolder(parent.inflateChild(R.layout.item_download_failed))
            }
            DownloadStatus.RUNNING -> {
                DownloadingViewHolder(parent.inflateChild(R.layout.item_download_downloading))
            }
            else -> {
                GenericViewHolder(parent.inflateChild(R.layout.item_download_generic))
            }
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

        private val dismissButton: Button = itemView.findViewById(R.id.dismissButton)

        override fun bindData(job: DownloadJob) {
            super.bindData(job)
            dismissButton.setOnClickListener { onDismiss(job) }
        }
    }

    internal inner class FailedViewHolder(itemView: View) : ViewHolder(itemView) {

        private val cancelButton: Button = itemView.findViewById(R.id.cancelButton)

        private val retryButton: Button = itemView.findViewById(R.id.retryButton)

        override fun bindData(job: DownloadJob) {
            super.bindData(job)
            cancelButton.setOnClickListener { onCancel(job) }
            retryButton.setOnClickListener { onRetry(job) }
        }
    }

    internal inner class DownloadingViewHolder(itemView: View) : ViewHolder(itemView) {

        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        private val cancelButton: Button = itemView.findViewById(R.id.cancelButton)

        private val pauseButton: Button = itemView.findViewById(R.id.pauseButton)

        override fun bindData(job: DownloadJob) {
            super.bindData(job)
            progressBar.max = job.taskList.size
            progressBar.progress = job.taskIndex
            cancelButton.setOnClickListener { onCancel(job) }
            pauseButton.setOnClickListener { onPause(job) }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<DownloadJob>() {

        override fun areItemsTheSame(oldItem: DownloadJob, newItem: DownloadJob): Boolean {
            return oldItem.bookId == newItem.bookId
        }

        override fun areContentsTheSame(oldItem: DownloadJob, newItem: DownloadJob): Boolean {
            return oldItem.status == newItem.status && oldItem.taskIndex == newItem.taskIndex
        }
    }
}
