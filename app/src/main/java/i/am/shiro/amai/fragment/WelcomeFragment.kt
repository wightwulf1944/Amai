package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import i.am.shiro.amai.Preferences
import i.am.shiro.amai.R
import i.am.shiro.amai.util.goToMain
import i.am.shiro.amai.util.goToStorageSetup
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        skipButton.setOnClickListener { onSkip() }
        nextButton.setOnClickListener { onNext() }
    }

    private fun onSkip() {
        goToMain()

        val primaryExternal = requireContext().getExternalFilesDir(null) ?: throw RuntimeException()
        Preferences.setStoragePath(primaryExternal.path)
        Preferences.setFirstRunDone()
    }

    private fun onNext() {
        goToStorageSetup()
    }
}
