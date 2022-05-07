package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import i.am.shiro.amai.AmaiPreferences
import i.am.shiro.amai.R
import i.am.shiro.amai.util.goToMain
import i.am.shiro.amai.util.goToStorageSetup
import kotlinx.android.synthetic.main.fragment_welcome.*
import org.koin.android.ext.android.inject

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private val preferences by inject<AmaiPreferences>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        skipButton.setOnClickListener { onSkip() }
        nextButton.setOnClickListener { onNext() }
    }

    private fun onSkip() {
        goToMain()

        val primaryExternal = requireContext().getExternalFilesDir(null) ?: throw RuntimeException()
        preferences.storagePath = primaryExternal.path
        preferences.isFirstRun = false
    }

    private fun onNext() {
        goToStorageSetup()
    }
}
