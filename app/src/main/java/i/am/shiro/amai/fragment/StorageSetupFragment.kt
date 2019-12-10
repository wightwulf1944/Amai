package i.am.shiro.amai.fragment

import android.content.Context
import android.os.Bundle
import android.text.format.Formatter
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import i.am.shiro.amai.Preferences
import i.am.shiro.amai.R
import i.am.shiro.amai.model.StorageOption
import i.am.shiro.amai.util.addChild
import kotlinx.android.synthetic.main.fragment_storage_setup.*

private const val KEY_SELECTED = "selectedIndex"

class StorageSetupFragment : Fragment(R.layout.fragment_storage_setup) {

    private lateinit var storageOptions: List<StorageOption>

    private var selectedIndex: Int = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)

        storageOptions = context.getStorageOptions()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt(KEY_SELECTED, -1)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED, selectedIndex)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        finishButton.isEnabled = selectedIndex != -1
        finishButton.setOnClickListener { onFinish() }

        storageOptions.forEachIndexed { i, storageOption ->

            optionsLayout.addChild<View>(R.layout.item_storage_option) {
                isSelected = selectedIndex == i
                setOnClickListener {
                    optionsLayout.dispatchSetSelected(false)
                    isSelected = true
                    selectedIndex = i
                    finishButton.isEnabled = true
                }

                findViewById<TextView>(R.id.titleText).text = getString(R.string.storage_label, i)

                findViewById<TextView>(R.id.pathText).text = storageOption.path

                val spaceFree = storageOption.spaceFree
                val freeSpaceSize = Formatter.formatFileSize(context, spaceFree)
                findViewById<TextView>(R.id.freeSpaceText).text = getString(R.string.storage_free, freeSpaceSize)

                findViewById<ProgressBar>(R.id.gauge).progress = storageOption.percentUsed
            }
        }
    }

    private fun onFinish() {
        parentFragmentManager.popBackStack()
        parentFragmentManager.commit { replace(android.R.id.content, MainFragment()) }

        Preferences.setStoragePath(storageOptions[selectedIndex].path)
        Preferences.setFirstRunDone()
    }
}

private fun Context.getStorageOptions() = getExternalFilesDirs(null).map { StorageOption(it) }