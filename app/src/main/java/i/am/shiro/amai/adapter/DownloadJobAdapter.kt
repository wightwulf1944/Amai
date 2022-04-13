package i.am.shiro.amai.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import i.am.shiro.amai.DownloadStatus
import i.am.shiro.amai.data.model.Download
import i.am.shiro.amai.databinding.ItemDownloadDoneBinding
import i.am.shiro.amai.databinding.ItemDownloadDownloadingBinding
import i.am.shiro.amai.databinding.ItemDownloadFailedBinding
import i.am.shiro.amai.databinding.ItemDownloadGenericBinding
import i.am.shiro.amai.util.inflateChild

class DownloadJobAdapter(
    private val onDismiss: (Download) -> Unit,
    private val onCancel: (Download) -> Unit,
    private val onRetry: (Download) -> Unit,
    private val onPause: (Download) -> Unit,
    private val onResume: (Download) -> Unit
) : ListAdapter<Download, DownloadJobAdapter.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int) = getItem(position).status.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (DownloadStatus.values()[viewType]) {
            DownloadStatus.DONE -> {
                DoneViewHolder(parent.inflateChild(ItemDownloadDoneBinding::inflate))
            }
            DownloadStatus.FAILED -> {
                FailedViewHolder(parent.inflateChild(ItemDownloadFailedBinding::inflate))
            }
            DownloadStatus.RUNNING -> {
                DownloadingViewHolder(parent.inflateChild(ItemDownloadDownloadingBinding::inflate))
            }
            else -> {
                GenericViewHolder(parent.inflateChild(ItemDownloadGenericBinding::inflate))
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = getItem(position)
        holder.bindData(job)
    }

    abstract class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        internal abstract fun bindData(job: Download)
    }

    internal class GenericViewHolder(private val binding: ItemDownloadGenericBinding) :
        ViewHolder(binding) {

        override fun bindData(job: Download) {
            with(binding) {
                titleText.text = job.title
                statusText.text = job.status.toString()
            }
        }
    }

    internal inner class DoneViewHolder(private val binding: ItemDownloadDoneBinding) :
        ViewHolder(binding) {

        override fun bindData(job: Download) {
            with(binding) {
                titleText.text = job.title
                dismissButton.setOnClickListener { onDismiss(job) }
            }
        }
    }

    internal inner class FailedViewHolder(private val binding: ItemDownloadFailedBinding) :
        ViewHolder(binding) {

        override fun bindData(job: Download) {
            with(binding) {
                binding.titleText.text = job.title
                cancelButton.setOnClickListener { onCancel(job) }
                retryButton.setOnClickListener { onRetry(job) }
            }
        }
    }

    internal inner class DownloadingViewHolder(private val binding: ItemDownloadDownloadingBinding) :
        ViewHolder(binding) {

        override fun bindData(job: Download) {
            with(binding) {
                binding.titleText.text = job.title
                progressBar.max = job.progressMax
                progressBar.progress = job.progress
                cancelButton.setOnClickListener { onCancel(job) }
                pauseButton.setOnClickListener { onPause(job) }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Download>() {

        override fun areItemsTheSame(oldItem: Download, newItem: Download) =
            oldItem.bookId == newItem.bookId

        override fun areContentsTheSame(oldItem: Download, newItem: Download) =
            oldItem.status == newItem.status && oldItem.progress == newItem.progress
    }
}
