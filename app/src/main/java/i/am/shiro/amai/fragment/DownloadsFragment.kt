package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.DownloadJobAdapter
import i.am.shiro.amai.fragment.dialog.PlaceholderDialogFragment
import i.am.shiro.amai.model.DownloadJob
import i.am.shiro.amai.util.show
import i.am.shiro.amai.viewmodel.DownloadsViewModel
import kotlinx.android.synthetic.main.fragment_downloads.*

class DownloadsFragment : Fragment(R.layout.fragment_downloads) {

    private val viewModel by viewModels<DownloadsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setOnMenuItemClickListener(::onActionClick)

        val adapter = DownloadJobAdapter(
                onDismiss = viewModel::dismissJob,
                onCancel = {
                    viewModel.cancelJob(it)
                    showPlaceholder()
                },
                onRetry = {
                    viewModel.retryJob(it)
                    showPlaceholder()
                },
                onPause = {
                    viewModel.pauseJob(it)
                    showPlaceholder()
                }
        )

        recyclerView.adapter = adapter

        viewModel.downloadJobsLive.observe(this, Observer(adapter::submitList))
    }

    private fun onActionClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_resume_all -> showPlaceholder()
            R.id.action_pause_all -> showPlaceholder()
        }
        return true
    }

    private fun showPlaceholder() {
        PlaceholderDialogFragment().show(childFragmentManager)
    }
}
