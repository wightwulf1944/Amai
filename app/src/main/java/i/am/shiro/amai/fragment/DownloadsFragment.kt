package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.DownloadJobAdapter
import i.am.shiro.amai.util.amaiViewModels
import i.am.shiro.amai.viewmodel.DownloadsViewModel
import kotlinx.android.synthetic.main.fragment_downloads.*

class DownloadsFragment : Fragment(R.layout.fragment_downloads) {

    private val viewModel by amaiViewModels<DownloadsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setOnMenuItemClickListener(::onActionClick)

        val adapter = DownloadJobAdapter(
            onDismiss = viewModel::dismiss,
            onCancel = viewModel::cancel,
            onRetry = viewModel::retry,
            onPause = viewModel::pause,
            onResume = viewModel::resume
        )

        recyclerView.adapter = adapter

        viewModel.downloadsLive.observe(viewLifecycleOwner, adapter::submitList)
    }

    private fun onActionClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_resume_all -> viewModel.resumeAll()
            R.id.action_pause_all -> viewModel.pauseAll()
        }
        return true
    }
}
